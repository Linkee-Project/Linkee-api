package com.linkee.linkeeapi.quiz_room.websocket.service;

import com.linkee.linkeeapi.category.command.aggregate.Category;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.question.command.domain.aggregate.Question;
import com.linkee.linkeeapi.question.command.infrastructure.repository.JpaQuestionRepository;
import com.linkee.linkeeapi.question_option.command.domain.aggregate.QuestionOption;
import com.linkee.linkeeapi.quiz_room.command.domain.aggregate.QuizRoom;
import com.linkee.linkeeapi.quiz_room.command.infrastructure.repository.QuizRoomRepository;
import com.linkee.linkeeapi.quiz_room.websocket.dto.QuizMessageType;
import com.linkee.linkeeapi.quiz_room.websocket.dto.response.QuizWebSocketResponse;
import com.linkee.linkeeapi.quiz_room.websocket.dto.response.data.AnswerSubmittedData;
import com.linkee.linkeeapi.quiz_room.websocket.dto.response.data.OptionInfo;
import com.linkee.linkeeapi.quiz_room.websocket.dto.response.data.QuizQuestionData;
import com.linkee.linkeeapi.user.command.application.service.util.UserFinder;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuizRoomWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;
    private final QuizRoomRepository quizRoomRepository;
    private final JpaQuestionRepository jpaQuestionRepository;

    private final UserFinder userFinder;

    /**
     * 퀴즈 시작:
     * - 방장 검증
     * - 방 카테고리 기준으로 검증(Y) + 삭제되지 않은 문제 중 N개 조회
     * - 첫 문제 브로드캐스트
     */
    @Transactional(readOnly = true)
    public void startQuiz(Long roomId, Long userId) {
        log.info("Attempting to start quiz for roomId={} by userId={}", roomId, userId);

        QuizRoom room = quizRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("QuizRoom not found: " + roomId));

        // 1️⃣ 방장만 시작 가능
        if (!room.getRoomOwner().getUserId().equals(userId)) {
            sendErrorToUser(userId, "퀴즈를 시작할 권한이 없습니다. (방장 아님)");
            return;
        }

        // 2️⃣ 방에 설정된 카테고리와 문제 개수 확인
        Category category = room.getCategory();
        int questionCount = room.getRoomQuizLimit() != null ? room.getRoomQuizLimit() : 10;

        // 3️⃣ 검증된(Y) 문제 중 삭제되지 않은(N) 문제 조회
        List<Question> qualified = jpaQuestionRepository
                .findByCategoryAndIsQualifiedAndIsDeleted(category, Status.Y, Status.N);

        if (qualified.isEmpty()) {
            sendErrorToUser(userId, "이 카테고리에는 검증된 문제가 없습니다.");
            return;
        }

        // 요청한 문제 개수만큼 자르기
        List<Question> selected = qualified.stream()
                .limit(questionCount)
                .toList();

        // 4️⃣ 첫 문제로 브로드캐스트
        Question first = selected.get(0);

        /*
         * 엔티티 → DTO 변환
         * - QuestionOption(엔티티) → OptionInfo(DTO)
         * - 엔티티가 WebSocket 응답으로 직접 나가는 것을 방지
         * - from() 메서드로 변환 로직 캡슐화
         */
        List<OptionInfo> options = first.getOptions().stream()
                .sorted(Comparator.comparing(QuestionOption::getOptionIndex))
                .map(OptionInfo::from)
                .toList();

        QuizQuestionData data = QuizQuestionData.builder()
                .questionNumber(1)
                .totalQuestions(selected.size())
                .questionId(first.getQuestionId())
                .questionContent(first.getQuestionQuestion())
                .categoryName(first.getCategory().getCategoryName()) // 카테고리명 추가
                .options(options)
                .timeLimit(30)
                .serverStartTime(LocalDateTime.now()) // 시작 시각
                .build();

        QuizWebSocketResponse response = QuizWebSocketResponse.builder()
                .type(QuizMessageType.QUESTION_STARTED)
                .success(true)
                .message("퀴즈가 시작됩니다!")
                .data(data)
                .build();

        String destination = "/sub/quiz-room/" + roomId;
        messagingTemplate.convertAndSend(destination, response);
        log.info("Broadcasting to {}: {}", destination, response);
    }

    /* 답안 제출 */
    @Transactional
    public void submitAnswer(Long roomId, Long userId, Long questionId, Long selectedOptionId) {
        log.info("Answer submitted in roomId={} by userId={}: questionId={}, selectedOptionId={}",
                roomId, userId, questionId, selectedOptionId);

        User user = userFinder.getById(userId);
        String nickname = user.getUserNickname();

        AnswerSubmittedData submittedData = AnswerSubmittedData.builder()
                .userId(userId)
                .userName(nickname)
                .submittedCount(1)
                .totalParticipants(5)
                .build();

        QuizWebSocketResponse response = QuizWebSocketResponse.builder()
                .type(QuizMessageType.ANSWER_SUBMITTED)
                .success(true)
                .message(submittedData.getUserName() + "님이 답안을 제출했습니다.")
                .data(submittedData)
                .build();

        String destination = "/sub/quiz-room/" + roomId;
        messagingTemplate.convertAndSend(destination, response);
        log.info("Broadcasting to {}: {}", destination, response);
    }

    /* 에러 메시지 전송 */
    public void sendErrorToUser(Long userId, String errorMessage) {
        QuizWebSocketResponse response = QuizWebSocketResponse.builder()
                .type(QuizMessageType.ERROR)
                .success(false)
                .message(errorMessage)
                .build();

        messagingTemplate.convertAndSendToUser(String.valueOf(userId), "/queue/errors", response);
        log.warn("Sending error to userId {}: {}", userId, errorMessage);
    }
}
