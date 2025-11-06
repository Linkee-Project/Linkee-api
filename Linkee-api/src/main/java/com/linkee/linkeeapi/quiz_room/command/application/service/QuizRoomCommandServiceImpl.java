package com.linkee.linkeeapi.quiz_room.command.application.service;

import com.linkee.linkeeapi.category.command.aggregate.Category;
import com.linkee.linkeeapi.category.command.infrastructure.repository.CategoryRepository;
import com.linkee.linkeeapi.common.enums.RoomStatus;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.grade.command.domain.aggregate.entity.Grade;
import com.linkee.linkeeapi.question.command.domain.aggregate.Question;
import com.linkee.linkeeapi.question.command.infrastructure.repository.JpaQuestionRepository;
import com.linkee.linkeeapi.quiz_current_index.command.domain.aggregate.QuizCurrentIndex;
import com.linkee.linkeeapi.quiz_current_index.command.infrastructure.repository.QuizCurrentIndexRepository;
import com.linkee.linkeeapi.quiz_room.command.application.dto.request.QuizRoomCreateRequestDto;
import com.linkee.linkeeapi.quiz_room.command.application.dto.request.QuizRoomDeleteRequestDto;
import com.linkee.linkeeapi.quiz_room.command.domain.aggregate.QuizRoom;
import com.linkee.linkeeapi.quiz_room.command.infrastructure.repository.QuizRoomRepository;
import com.linkee.linkeeapi.quiz_room.command.infrastructure.scheduler.QuizGameAdvanceScheduler;
import com.linkee.linkeeapi.quiz_room.query.dto.response.ResultRowResponseDto;
import com.linkee.linkeeapi.quiz_room.query.service.QuizRoomQueryService;
import com.linkee.linkeeapi.room_member.command.domain.aggregate.RoomMember;
import com.linkee.linkeeapi.room_member.command.infrastructure.repository.RoomMemberRepository;
import com.linkee.linkeeapi.room_question.command.application.dto.request.RoomQuestionCreateRequest;
import com.linkee.linkeeapi.room_question.command.application.service.RoomQuestionCommandService;
import com.linkee.linkeeapi.room_question.command.infrastructure.repository.RoomQuestionRepository;
import com.linkee.linkeeapi.room_user_log.command.domain.aggregate.RoomUserLog;
import com.linkee.linkeeapi.room_user_log.command.infrastructure.repository.JpaRoomUserLogRepository;
import com.linkee.linkeeapi.user.command.application.service.util.UserFinder;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user_grade.command.domain.entity.UserGrade;
import com.linkee.linkeeapi.user_grade.command.infrastructure.repository.UserGradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/*
 * 퀴즈룸 생성 관련 비즈니스 로직을 처리하는 서비스 구현체.
 * {@link QuizRoomCommandService} 인터페이스를 구현합니다.
 * 트랜잭션 관리를 포함하여 퀴즈룸 생성의 전체 흐름을 제어합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 읽기 전용 트랜잭션 설정
public class QuizRoomCommandServiceImpl implements QuizRoomCommandService {

    private final QuizRoomRepository quizRoomRepository;
    private final CategoryRepository categoryRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final QuizCurrentIndexRepository quizCurrentIndexRepository;
    private final UserFinder userFinder;
    private final JpaQuestionRepository jpaQuestionRepository;
    private final RoomQuestionCommandService roomQuestionCommandService;
    private final QuizGameAdvanceScheduler quizGameAdvanceScheduler;
    private final JpaRoomUserLogRepository jpaRoomUserLogRepository;
    private final RoomQuestionRepository roomQuestionRepository;
    private final QuizRoomQueryService  quizRoomQueryService;
    private final UserGradeRepository userGradeRepository;




    /*
     * 새로운 퀴즈룸을 생성합니다.
     * 요청 DTO와 사용자 ID를 기반으로 퀴즈룸 엔티티를 구성하고 저장합니다.
     * @param request 퀴즈룸 생성에 필요한 데이터를 담은 DTO
     * @param userId 퀴즈룸을 생성하는 사용자의 ID (방장)
     * @return 생성된 퀴즈룸의 고유 ID
     * @throws IllegalArgumentException 사용자 또는 카테고리를 찾을 수 없을 경우 발생
     */
    @Override
    @Transactional // 쓰기 작업이므로 별도의 트랜잭션 설정
    public Long create(QuizRoomCreateRequestDto request, Long userId) {
        // 1. 퀴즈룸을 생성하는 사용자(방장) 정보를 조회합니다.
        // 사용자가 존재하지 않으면 예외를 발생시킵니다.
        User roomOwner = userFinder.getById(userId);

        // 2. 요청된 카테고리 ID에 해당하는 카테고리 정보를 조회합니다.
        // 카테고리가 존재하지 않으면 예외를 발생시킵니다.
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        // 3. Builder 패턴을 사용하여 새로운 QuizRoom 엔티티를 생성합니다.
        // DTO와 조회된 엔티티 정보를 바탕으로 필드를 설정합니다.
        QuizRoom newQuizRoom = QuizRoom.builder()
                .category(category) // 조회된 카테고리 엔티티 설정
                .roomOwner(roomOwner) // 조회된 사용자 엔티티 설정
                .roomTitle(request.getRoomTitle())
                .roomStatus(RoomStatus.W) // 퀴즈룸의 초기 상태는 '대기(Waiting)'로 설정
                .roomMode(request.getRoomMode())
                .joinedCount(1) // 방장 포함 1명으로 시작
                .roomCapacity(request.getRoomCapacity())
                .isPrivate(request.getIsPrivate())
                .roomCode(request.getRoomCode()) // 비공개방일 경우 입장 코드 설정 (null 가능)
                .roomQuizLimit(request.getRoomQuizLimit())
                .build();

        // 4. 구성된 QuizRoom 엔티티를 데이터베이스에 저장합니다.
        quizRoomRepository.save(newQuizRoom);

        // 5. 성공적으로 생성된 퀴즈룸의 고유 ID를 반환합니다.
        return newQuizRoom.getQuizRoomId();
    }


    private void endGame(QuizRoom quizRoom) {
        //  1. 퀴즈방 상태를 '종료(E)'로 변경하고 종료 시간 기록
        quizRoom.setRoomStatus(RoomStatus.E);
        quizRoom.setEndedAt(LocalDateTime.now());
        quizRoomRepository.save(quizRoom);

        //  2. QuizCurrentIndex 데이터가 있다면 삭제
        quizCurrentIndexRepository.findByQuizRoom(quizRoom)
                .ifPresent(quizCurrentIndexRepository::delete);

        //  3. 게임 결과 정산 및 승리 횟수 업데이트
        //  3-1. 게임 결과 조회(정답 수 기준 내림차순)
        List<ResultRowResponseDto> results = quizRoomQueryService.getResultsByRoom(quizRoom.getQuizRoomId(), 0, 1);

        //  3-2. 우승자 선정 (결과아 있고, 정답 수가 0 보다 큰 경우)
        if (!results.isEmpty() && results.get(0).getCorrectCount() > 0) {
            ResultRowResponseDto winnerResult = results.get(0);
            User winner = userFinder.getById(winnerResult.getUserId());
            Category category = quizRoom.getCategory();

            //  3-3. 해당 유저의 카테고리별 등급 정보 조회 또는 생성
            UserGrade userGrade = userGradeRepository.findByUserAndCategory(winner, category)
                    .orElseGet(() -> {
                        //  등급 정보가 없으면 새로 생성 (기본 등급, 승리 횟수 0)
                        //  기본 등급(ID=1L)이 DB에 존재한다고 가정
                        Grade defaultGrade = Grade.builder().gradeId(1L).build();
                        return UserGrade.builder()
                                .user(winner)
                                .category(category)
                                .grade(defaultGrade)
                                .victoryCount(0)
                                .build();
                    });
            //  3-4. 승리 횟수 1 증가 및 저장
            userGrade.modifyVictoryCount(userGrade.getVictoryCount() + 1);
            userGradeRepository.save(userGrade);
        }

    }



    @Override
    @Transactional
    public void leaveQuizRoom(QuizRoomDeleteRequestDto request) {
        QuizRoom quizRoom = quizRoomRepository.findById(request.getQuizRoomId())
                .orElseThrow(() -> new BusinessException(ErrorCode.QUIZ_ROOM_NOT_FOUND));

        //  게임 중에는 아무도 나갈 수 없음
        if(quizRoom.getRoomStatus() == RoomStatus.P) {
            throw new BusinessException(ErrorCode.QUIZ_ROOM_GAME_IN_PLAY);

        }
        // 대기 상태에서 나가는 경우
        if(quizRoom.getRoomOwner().getUserId().equals(request.getUserId())) {

            // 방장이 나가면 방 자동 삭제
            endGame(quizRoom);
        } else {
            //  일반 멤버가 나가면 인원수만 감소
            quizRoom.setJoinedCount(quizRoom.getJoinedCount() - 1);
            quizRoomRepository.save(quizRoom);
        }

    }

    // 게임 시작
    @Override
    @Transactional
    public void startGame(Long quizRoomId, Long userId) {
        //  1. 퀴즈방을 조회합니다.
        QuizRoom quizRoom = quizRoomRepository.findById(quizRoomId)
                .orElseThrow(() -> new BusinessException(ErrorCode.QUIZ_ROOM_NOT_FOUND));
        // 2. 게임 시작 전제조건을 확인
        // 방장만 시작 할 수 있다
        if(!quizRoom.getRoomOwner().getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.QUIZ_ROOM_UNAUTHORIZED);
        }

        // 2-1. 방 상태가 대기(W) 상태인지 확인
        if (quizRoom.getRoomStatus() != RoomStatus.W) {
            throw new BusinessException(ErrorCode.QUIZ_ROOM_NOT_WAITING);
        }
        //  2-2. 해당 퀴즈방의 모든 멤버가 준비 상태인지 확인
        List<RoomMember> members = roomMemberRepository.findByQuizRoom(quizRoom);
        if (members.stream().anyMatch(member -> !member.isReady())){
            throw new BusinessException(ErrorCode.QUIZ_ROOM_NOT_READY);
        }
        //  3. 문제 선정 및 방에 할당
        //  3-1. 해당 카테고리에서 검증 된 문 제 목록 가져옴
        List<Question> qualifiedQuestions = jpaQuestionRepository.findByCategoryAndIsQualifiedAndIsDeleted(
                quizRoom.getCategory(), Status.Y, Status.N
        );
        //  3-2. 문제 목록을 랜덤으로 섞음
        Collections.shuffle(qualifiedQuestions);

        //  3-3. 필요한 만큼의 문제를 방에 할다 (quizOrder 설정)
        for (int i = 0; i < quizRoom.getRoomQuizLimit(); i++) {
            Question selectedQuestion = qualifiedQuestions.get(i);
            RoomQuestionCreateRequest createRequest = RoomQuestionCreateRequest.builder()
                    .quizRoomId(quizRoomId)
                    .questionId(selectedQuestion.getQuestionId())
                    .quizOrder(i + 1)   // 1부터 순서대로
                    .build();
            roomQuestionCommandService.createRoomQuestion(createRequest);

        }

        //  4. 퀴즈방의 상태를 '진행'으로 변경합니다.
        quizRoom.setRoomStatus(RoomStatus.P);
        quizRoomRepository.save(quizRoom);

        //  5. QuizCurrentIndex 레코드를 생성, 현재 문제 번호룰 1로 설정
        QuizCurrentIndex newQuizIndex = QuizCurrentIndex.builder()
                .quizRoom(quizRoom)
                .currentQuizIndex(1)
                .build();
        quizCurrentIndexRepository.save(newQuizIndex);

        // 첫 번째 문제의 타이머를 스케줄링 합니다.
        quizGameAdvanceScheduler.scheduleAdvanceQuestion(quizRoomId, 30 * 1000L);
    }

    @Override
    @Transactional
    public void advanceNextQuestion(Long quizRoomId) {
        //  1. 퀴즈방과 현재 문제 인덱스를 조회
        QuizRoom quizRoom = quizRoomRepository.findById(quizRoomId)
                .orElseThrow(() -> new BusinessException(ErrorCode.QUIZ_ROOM_NOT_FOUND));
        QuizCurrentIndex quizIndex = quizCurrentIndexRepository.findByQuizRoom(quizRoom)
                .orElseThrow(() -> new BusinessException(ErrorCode.QUIZ_INDEX_NOT_FOUND));

        //  2. 현재 문제에 대한 RoomQuestion 엔티티를 찾음
        roomQuestionRepository.findByQuizRoomAndQuizOrder(quizRoom, quizIndex.getCurrentQuizIndex())
                .ifPresent(currentRoomQuestion -> {
                    //3. 이 방의 모든 멤버를 조회
                            List<RoomMember> members = roomMemberRepository.findByQuizRoom(quizRoom);
                            for (RoomMember member : members) {
                                //  4. 각 멤버가 현재 문제에 대해 답변했는지 확인
                                jpaRoomUserLogRepository.findByRoomMemberAndRoomQuestion(member, currentRoomQuestion)
                                        .ifPresentOrElse(
                                                log -> {},  //  답변 기록이 있으면 아무것도 안 함
                                                () -> { //  답변 기록이 없으면 오답으로 기록
                                                    RoomUserLog unansweredLog = RoomUserLog
                                                            .builder()
                                                            .roomMember(member)
                                                            .roomQuestion(currentRoomQuestion)
                                                            .isCorrected(Status.N)  // 오답처리
                                                            .build();
                                                    jpaRoomUserLogRepository.save(unansweredLog);
                                                }
                                        );
                            }
                });

        //  5. 마지막 문제인지 확인합니다.
        if(quizIndex.getCurrentQuizIndex() >= quizRoom.getRoomQuizLimit()) {
            //  마지막 문제이므로, 게임을 종료시킵니다.
            endGame(quizRoom);
            return; // 종료 후 아래 로직을 실행하지 않고 종료 (자동 종료)
        }
        //  6. 현재 문제 인덱스를 1 증가시킵니다.
        quizIndex.setCurrentQuizIndex(quizIndex.getCurrentQuizIndex() + 1);
        quizCurrentIndexRepository.save(quizIndex);

        //  7. 다음 문제의 타이머를 스케줄링 합니다 (30초)
        quizGameAdvanceScheduler.scheduleAdvanceQuestion(quizRoomId, 30 * 1000L);
    }

    @Override
    @Transactional
    public void forceEndRoom(Long quizRoomId) {
        QuizRoom quizRoom = quizRoomRepository.findById(quizRoomId)
                .orElseThrow(()->  new BusinessException(ErrorCode.QUIZ_ROOM_NOT_FOUND));

        endGame(quizRoom);
    }

}
