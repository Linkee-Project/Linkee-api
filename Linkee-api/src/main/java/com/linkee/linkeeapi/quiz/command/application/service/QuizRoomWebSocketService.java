package com.linkee.linkeeapi.quiz.command.application.service;

import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.question.command.domain.aggregate.Question;
import com.linkee.linkeeapi.question.command.domain.aggregate.QuestionOption;
import com.linkee.linkeeapi.quiz.command.application.dto.response.data.*;
import com.linkee.linkeeapi.quiz.command.domain.aggregate.QuizCurrentIndex;
import com.linkee.linkeeapi.quiz.command.infrastructure.repository.QuizCurrentIndexRepository;
import com.linkee.linkeeapi.quiz.command.domain.aggregate.QuizRoom;
import com.linkee.linkeeapi.quiz.command.infrastructure.repository.QuizRoomRepository;
import com.linkee.linkeeapi.quiz.query.dto.response.ResultRowResponseDto;
import com.linkee.linkeeapi.quiz.query.service.QuizRoomQueryService;
import com.linkee.linkeeapi.quiz.command.application.dto.QuizMessageType;
import com.linkee.linkeeapi.quiz.command.application.dto.response.QuizWebSocketResponse;
import com.linkee.linkeeapi.quiz.command.domain.aggregate.RoomMember;
import com.linkee.linkeeapi.quiz.command.infrastructure.repository.RoomMemberRepository;
import com.linkee.linkeeapi.quiz.query.dto.request.RoomMemberSearchRequest;
import com.linkee.linkeeapi.quiz.query.dto.response.RoomMemberResponse;
import com.linkee.linkeeapi.quiz.query.service.RoomMemberQueryService;
import com.linkee.linkeeapi.quiz.command.domain.aggregate.RoomQuestion;
import com.linkee.linkeeapi.quiz.command.infrastructure.repository.RoomQuestionRepository;
import com.linkee.linkeeapi.quiz.command.domain.aggregate.RoomUserLog;
import com.linkee.linkeeapi.quiz.command.infrastructure.repository.JpaRoomUserLogRepository;

import com.linkee.linkeeapi.users.command.application.service.util.UserFinder;
import com.linkee.linkeeapi.users.command.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuizRoomWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;
    private final QuizRoomRepository quizRoomRepository;
    private final RoomQuestionRepository roomQuestionRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final JpaRoomUserLogRepository jpaRoomUserLogRepository;
    private final QuizRoomQueryService quizRoomQueryService;
    private final QuizCurrentIndexRepository quizCurrentIndexRepository;
    private final RoomMemberQueryService roomMemberQueryService;
    private final UserFinder userFinder;

    /*
     * 퀴즈 시작: 첫 문제 브로드캐스트
     * quizCommandService_startGame()에서 호출됨)
     */
    @Transactional(readOnly = true)
    public void startQuiz(Long roomId, Long userId) {
        log.info("Starting quiz for roomId={} by userId={}", roomId, userId);

        QuizRoom room = quizRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("QuizRoom not found: " + roomId));

        // ✅ 첫 번째 문제 조회 (quizOrder = 1)
        RoomQuestion firstRoomQuestion = roomQuestionRepository
                .findByQuizRoomAndQuizOrder(room, 1)
                .orElseThrow(() -> new IllegalArgumentException("First question not found"));

        Question firstQuestion = firstRoomQuestion.getQuestion();

        // ✅ QuestionOption → OptionInfo 변환
        List<OptionInfo> options = firstQuestion.getOptions().stream()
                .sorted(Comparator.comparing(QuestionOption::getOptionIndex))
                .map(OptionInfo::from)
                .toList();

        QuizQuestionData data = QuizQuestionData.builder()
                .questionNumber(1)
                .totalQuestions(room.getRoomQuizLimit())
                .questionId(firstQuestion.getQuestionId())
                .questionContent(firstQuestion.getQuestionQuestion())
                .categoryName(firstQuestion.getCategory().getCategoryName())
                .options(options)
                .timeLimit(30)
                .serverStartTime(LocalDateTime.now())
                .build();

        QuizWebSocketResponse response = QuizWebSocketResponse.builder()
                .type(QuizMessageType.QUESTION_STARTED)
                .success(true)
                .message("퀴즈가 시작됩니다!")
                .data(data)
                .build();

        messagingTemplate.convertAndSend("/sub/quiz-room/" + roomId, response);
        log.info("✅ First question broadcasted to roomId={}", roomId);
    }

    /**
     * 답안 제출 알림 브로드캐스트
     */
    @Transactional(readOnly = true)
    public void notifyAnswerSubmitted(Long roomId, Long userId) {
        log.info("Answer submitted: roomId={}, userId={}", roomId, userId);

        // 사용자 정보 조회
        User user = userFinder.getById(userId);

        // 현재 제출 인원 계산
        QuizRoom room = quizRoomRepository.findById(roomId).orElseThrow();
        QuizCurrentIndex quizIndex = quizCurrentIndexRepository.findByQuizRoom(room).orElseThrow();
        RoomQuestion currentQuestion = roomQuestionRepository
                .findByQuizRoomAndQuizOrder(room, quizIndex.getCurrentQuizIndex())
                .orElseThrow();

        long submittedCount = jpaRoomUserLogRepository.countByRoomQuestion(currentQuestion);
        int totalParticipants = room.getJoinedCount();

        AnswerSubmittedData data = AnswerSubmittedData.builder()
                .userId(userId)
                .userName(user.getUserNickname())
                .submittedCount((int) submittedCount)
                .totalParticipants(totalParticipants)
                .build();

        QuizWebSocketResponse response = QuizWebSocketResponse.builder()
                .type(QuizMessageType.ANSWER_SUBMITTED)
                .success(true)
                .message(user.getUserNickname() + "님이 답안을 제출했습니다.")
                .data(data)
                .build();

        messagingTemplate.convertAndSend("/sub/quiz-room/" + roomId, response);
        log.info("✅ Answer submission broadcasted to roomId={}", roomId);
    }
    /*
     * 문제 결과 브로드캐스트 (30초 후 스케줄러에서 호출)
     */
    @Transactional(readOnly = true)
    public void broadcastQuestionResult(Long roomId, Integer questionNumber) {
        log.info("Broadcasting question result: roomId={}, questionNumber={}", roomId, questionNumber);

        QuizRoom room = quizRoomRepository.findById(roomId).orElseThrow();
        RoomQuestion roomQuestion = roomQuestionRepository
                .findByQuizRoomAndQuizOrder(room, questionNumber)
                .orElseThrow();

        Question question = roomQuestion.getQuestion();

        // ✅ 정답 찾기 (isCorrected = Y)
        QuestionOption correctOption = question.getOptions().stream()
                .filter(opt -> opt.getIsCorrected() == Status.Y)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("정답이 설정되지 않음"));

        // 각 참가자의 답변 결과 조회
        List<RoomMember> members = roomMemberRepository.findByQuizRoom(room);
        List<QuizUserResult> userResults = members.stream()
                .map(member -> {
                    RoomUserLog log = jpaRoomUserLogRepository
                            .findByRoomMemberAndRoomQuestion(member, roomQuestion)
                            .orElse(null);

                    return QuizUserResult.builder()
                            .userId(member.getMember().getUserId())
                            .userName(member.getMember().getUserNickname())
                            .selectedOptionId(null) // 선택값 저장 안함
                            .isCorrect(log != null && log.getIsCorrected() == Status.Y)
                            .responseTime(5) // 결과 표시 시간
                            .build();
                })
                .toList();

        QuizResultData data = QuizResultData.builder()
                .correctOptionId(correctOption.getQuestionOptionId())
                .correctOptionText(correctOption.getOptionText())
                .userResults(userResults)
                .build();

        QuizWebSocketResponse response = QuizWebSocketResponse.builder()
                .type(QuizMessageType.QUESTION_RESULT)
                .success(true)
                .message("문제 결과입니다.")
                .data(data)
                .build();

        messagingTemplate.convertAndSend("/sub/quiz-room/" + roomId, response);
    }

    /*
     * 다음 문제 브로드캐스트
     */
    @Transactional(readOnly = true)
    public void broadcastNextQuestion(Long roomId, Integer questionNumber) {
        log.info("Broadcasting next question: roomId={}, questionNumber={}", roomId, questionNumber);

        QuizRoom room = quizRoomRepository.findById(roomId).orElseThrow();
        RoomQuestion roomQuestion = roomQuestionRepository
                .findByQuizRoomAndQuizOrder(room, questionNumber)
                .orElseThrow();

        Question question = roomQuestion.getQuestion();

        List<OptionInfo> options = question.getOptions().stream()
                .sorted(Comparator.comparing(QuestionOption::getOptionIndex))
                .map(OptionInfo::from)
                .toList();

        QuizQuestionData data = QuizQuestionData.builder()
                .questionNumber(questionNumber)
                .totalQuestions(room.getRoomQuizLimit())
                .questionId(question.getQuestionId())
                .questionContent(question.getQuestionQuestion())
                .categoryName(question.getCategory().getCategoryName())
                .options(options)
                .timeLimit(30)
                .serverStartTime(LocalDateTime.now())
                .build();

        QuizWebSocketResponse response = QuizWebSocketResponse.builder()
                .type(QuizMessageType.QUESTION_STARTED)
                .success(true)
                .message("다음 문제입니다!")
                .data(data)
                .build();

        messagingTemplate.convertAndSend("/sub/quiz-room/" + roomId, response);
    }

    /**
     * 게임 종료 + 최종 순위 브로드캐스트
     */
    @Transactional(readOnly = true)
    public void broadcastQuizFinished(Long roomId) {
        log.info("Broadcasting quiz finished: roomId={}", roomId);

        List<ResultRowResponseDto> results = quizRoomQueryService.getResultsByRoom(roomId, 0, 100);

        // ✅ 순위 계산
        List<QuizRanking> rankings = new ArrayList<>();
        int currentRank = 1;

        for (int i = 0; i < results.size(); i++) {
            ResultRowResponseDto result = results.get(i);

            // 동점자 처리
            if (i > 0 && !results.get(i - 1).getCorrectCount().equals(result.getCorrectCount())) {
                currentRank = i + 1;
            }

            rankings.add(QuizRanking.builder()
                    .rank(currentRank)
                    .userId(result.getUserId())
                    .userName(result.getUserNickname())
                    .correctCount(result.getCorrectCount())
                    .isMe(false)
                    .build());
        }

        // ✅ total은 모든 사용자가 동일하므로 첫 번째 것 사용
        int totalQuestions = results.isEmpty() ? 0 : results.get(0).getTotal();

        QuizRankingData data = QuizRankingData.builder()
                .totalQuestions(totalQuestions)
                .rankings(rankings)
                .build();

        QuizWebSocketResponse response = QuizWebSocketResponse.builder()
                .type(QuizMessageType.QUIZ_FINISHED)
                .success(true)
                .message("퀴즈가 종료되었습니다!")
                .data(data)
                .build();

        messagingTemplate.convertAndSend("/sub/quiz-room/" + roomId, response);
        log.info("✅ Quiz finished broadcasted to roomId={}", roomId);
    }


    /*
     * 멤버 목록 갱신 브로드캐스트 (준비 상태 변경 시)
     */
    @Transactional(readOnly = true)
    public void broadcastMemberList(Long roomId) {
        log.info("Broadcasting member list update: roomId={}", roomId);

        // RoomMemberQueryService 사용해서 멤버 목록 조회
        List<RoomMemberResponse> members = roomMemberQueryService.selectAllRoomMember(
                RoomMemberSearchRequest.builder().quizRoomId(roomId).build()
        );

        QuizWebSocketResponse response = QuizWebSocketResponse.builder()
                .type(QuizMessageType.MEMBER_UPDATED)
                .success(true)
                .message("멤버 목록이 갱신되었습니다.")
                .data(members)
                .build();

        messagingTemplate.convertAndSend("/sub/quiz-room/" + roomId, response);
        log.info("✅ Member list broadcasted to roomId={}", roomId);
    }
}