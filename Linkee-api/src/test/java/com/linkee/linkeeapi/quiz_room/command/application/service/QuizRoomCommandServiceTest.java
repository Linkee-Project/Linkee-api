package com.linkee.linkeeapi.quiz_room.command.application.service;

import com.linkee.linkeeapi.category.command.aggregate.Category;
import com.linkee.linkeeapi.category.command.infrastructure.repository.CategoryRepository;
import com.linkee.linkeeapi.common.enums.RoomMode;
import com.linkee.linkeeapi.common.enums.RoomStatus;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.question.command.domain.aggregate.Question;
import com.linkee.linkeeapi.question.command.infrastructure.repository.JpaQuestionRepository;
import com.linkee.linkeeapi.quiz_current_index.command.domain.aggregate.QuizCurrentIndex;
import com.linkee.linkeeapi.quiz_current_index.command.infrastructure.repository.QuizCurrentIndexRepository;
import com.linkee.linkeeapi.quiz_room.command.application.dto.request.QuizRoomCreateRequestDto;
import com.linkee.linkeeapi.quiz_room.command.application.dto.request.QuizRoomDeleteRequestDto;
import com.linkee.linkeeapi.quiz_room.command.domain.aggregate.QuizRoom;
import com.linkee.linkeeapi.quiz_room.command.infrastructure.repository.QuizRoomRepository;
import com.linkee.linkeeapi.quiz_room.command.infrastructure.scheduler.QuizGameAdvanceScheduler;
import com.linkee.linkeeapi.quiz_room.query.service.QuizRoomQueryService;
import com.linkee.linkeeapi.room_member.command.domain.aggregate.RoomMember;
import com.linkee.linkeeapi.room_member.command.infrastructure.repository.RoomMemberRepository;
import com.linkee.linkeeapi.room_question.command.application.service.RoomQuestionCommandService;
import com.linkee.linkeeapi.room_question.command.domain.aggregate.RoomQuestion;
import com.linkee.linkeeapi.room_question.command.infrastructure.repository.RoomQuestionRepository;
import com.linkee.linkeeapi.room_user_log.command.infrastructure.repository.JpaRoomUserLogRepository;
import com.linkee.linkeeapi.user.command.application.service.util.UserFinder;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user_grade.command.infrastructure.repository.UserGradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class QuizRoomCommandServiceTest {

    @Mock
    private QuizRoomRepository quizRoomRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserFinder userFinder;

    @Mock
    private QuizCurrentIndexRepository quizCurrentIndexRepository;

    @Mock
    private QuizRoomQueryService quizRoomQueryService;

    @Mock
    private UserGradeRepository userGradeRepository;

    @Mock
    private RoomMemberRepository roomMemberRepository;

    @Mock
    private JpaQuestionRepository jpaQuestionRepository;

    @Mock
    private RoomQuestionCommandService roomQuestionCommandService;

    @Mock
    private QuizGameAdvanceScheduler quizGameAdvanceScheduler;

    @Mock
    private RoomQuestionRepository roomQuestionRepository;

    @Mock
    private JpaRoomUserLogRepository jpaRoomUserLogRepository;

    @InjectMocks
    private QuizRoomCommandServiceImpl quizRoomCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("퀴즈방 생성 성공 테스트")
    void createQuizRoom_Success() {
        // given
        QuizRoomCreateRequestDto request = new QuizRoomCreateRequestDto(
                1L,
                "테스트 퀴즈방",
                RoomMode.G,
                Status.N,
                10,
                5,
                null
        );

        User roomOwner = User.builder()
                .userId(1L)
                .userNickname("방장")
                .build();

        Category category = Category.builder()
                .categoryId(1L)
                .categoryName("일반")
                .build();

        when(userFinder.getById(anyLong())).thenReturn(roomOwner);
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(quizRoomRepository.save(any(QuizRoom.class))).thenAnswer(invocation -> {
            QuizRoom room = invocation.getArgument(0);
            room.setQuizRoomId(1L);
            return room;
        });

        // when
        Long quizRoomId = quizRoomCommandService.create(request, 1L);

        // then
        verify(userFinder, times(1)).getById(1L);
        verify(categoryRepository, times(1)).findById(1L);
        verify(quizRoomRepository, times(1)).save(any(QuizRoom.class));
        assertThat(quizRoomId).isEqualTo(1L);
    }

    @Test
    @DisplayName("방장이 퀴즈방 나가기 성공 테스트 (방 종료)")
    void leaveQuizRoom_OwnerLeaves_EndsGame() {
        // given
        Long quizRoomId = 1L;
        Long ownerId = 1L;
        QuizRoomDeleteRequestDto request = new QuizRoomDeleteRequestDto(quizRoomId, ownerId);

        User roomOwner = User.builder().userId(ownerId).build();
        QuizRoom quizRoom = QuizRoom.builder()
                .quizRoomId(quizRoomId)
                .roomOwner(roomOwner)
                .roomStatus(RoomStatus.W)
                .build();

        when(quizRoomRepository.findById(quizRoomId)).thenReturn(Optional.of(quizRoom));
        when(quizRoomQueryService.getResultsByRoom(anyLong(), anyInt(), anyInt())).thenReturn(Collections.emptyList());

        // when
        quizRoomCommandService.leaveQuizRoom(request);

        // then
        verify(quizRoomRepository).findById(quizRoomId);
        verify(quizRoomRepository).save(quizRoom);
        assertThat(quizRoom.getRoomStatus()).isEqualTo(RoomStatus.E);
    }

    @Test
    @DisplayName("멤버가 퀴즈방 나가기 성공 테스트")
    void leaveQuizRoom_MemberLeaves_DecrementsCount() {
        // given
        Long quizRoomId = 1L;
        Long memberId = 2L;
        QuizRoomDeleteRequestDto request = new QuizRoomDeleteRequestDto(quizRoomId, memberId);

        User roomOwner = User.builder().userId(1L).build();
        QuizRoom quizRoom = QuizRoom.builder()
                .quizRoomId(quizRoomId)
                .roomOwner(roomOwner)
                .joinedCount(5)
                .roomStatus(RoomStatus.W)
                .build();

        when(quizRoomRepository.findById(quizRoomId)).thenReturn(Optional.of(quizRoom));

        // when
        quizRoomCommandService.leaveQuizRoom(request);

        // then
        verify(quizRoomRepository).findById(quizRoomId);
        verify(quizRoomRepository).save(quizRoom);
        assertThat(quizRoom.getJoinedCount()).isEqualTo(4);
    }

    @Test
    @DisplayName("게임중 퀴즈방 나가기 실패 테스트")
    void leaveQuizRoom_GameInPlay_ThrowsException() {
        // given
        Long quizRoomId = 1L;
        Long memberId = 2L;
        QuizRoomDeleteRequestDto request = new QuizRoomDeleteRequestDto(quizRoomId, memberId);

        User roomOwner = User.builder().userId(1L).build();
        QuizRoom quizRoom = QuizRoom.builder()
                .quizRoomId(quizRoomId)
                .roomOwner(roomOwner)
                .roomStatus(RoomStatus.P)
                .build();

        when(quizRoomRepository.findById(quizRoomId)).thenReturn(Optional.of(quizRoom));

        // when & then
        assertThrows(BusinessException.class, () -> {
            quizRoomCommandService.leaveQuizRoom(request);
        });
    }

    @Test
    @DisplayName("게임 시작 성공 테스트")
    void startGame_Success() {
        // given
        Long quizRoomId = 1L;
        Long userId = 1L;
        User roomOwner = User.builder().userId(userId).build();
        Category category = Category.builder().categoryId(1L).build();
        QuizRoom quizRoom = QuizRoom.builder()
                .quizRoomId(quizRoomId)
                .roomOwner(roomOwner)
                .category(category)
                .roomStatus(RoomStatus.W)
                .roomQuizLimit(5)
                .build();

        List<RoomMember> members = Collections.singletonList(RoomMember.builder().isReady(Status.Y).build());
        List<Question> questions = IntStream.range(0, 5)
                .mapToObj(i -> Question.builder().questionId((long) i).build())
                .collect(Collectors.toList());

        when(quizRoomRepository.findById(quizRoomId)).thenReturn(Optional.of(quizRoom));
        when(roomMemberRepository.findByQuizRoom(quizRoom)).thenReturn(members);
        when(jpaQuestionRepository.findByCategoryAndIsQualifiedAndIsDeleted(any(), any(), any())).thenReturn(questions);

        // when
        quizRoomCommandService.startGame(quizRoomId, userId);

        // then
        verify(quizRoomRepository).save(quizRoom);
        assertThat(quizRoom.getRoomStatus()).isEqualTo(RoomStatus.P);
        verify(roomQuestionCommandService, times(5)).createRoomQuestion(any());
        verify(quizCurrentIndexRepository).save(any(QuizCurrentIndex.class));
        verify(quizGameAdvanceScheduler).scheduleAdvanceQuestion(quizRoomId, 30 * 1000L);
    }

    @Test
    @DisplayName("방장이 아닌 유저가 게임 시작시 실패 테스트")
    void startGame_NotOwner_ThrowsException() {
        // given
        Long quizRoomId = 1L;
        Long userId = 2L; // Not the owner
        User roomOwner = User.builder().userId(1L).build();
        QuizRoom quizRoom = QuizRoom.builder()
                .quizRoomId(quizRoomId)
                .roomOwner(roomOwner)
                .build();

        when(quizRoomRepository.findById(quizRoomId)).thenReturn(Optional.of(quizRoom));

        // when & then
        assertThrows(BusinessException.class, () -> {
            quizRoomCommandService.startGame(quizRoomId, userId);
        });
    }

    @Test
    @DisplayName("대기 상태가 아닌 방에서 게임 시작시 실패 테스트")
    void startGame_NotWaiting_ThrowsException() {
        // given
        Long quizRoomId = 1L;
        Long userId = 1L;
        User roomOwner = User.builder().userId(userId).build();
        QuizRoom quizRoom = QuizRoom.builder()
                .quizRoomId(quizRoomId)
                .roomOwner(roomOwner)
                .roomStatus(RoomStatus.P) // Not in waiting status
                .build();

        when(quizRoomRepository.findById(quizRoomId)).thenReturn(Optional.of(quizRoom));

        // when & then
        assertThrows(BusinessException.class, () -> {
            quizRoomCommandService.startGame(quizRoomId, userId);
        });
    }

    @Test
    @DisplayName("모든 멤버가 준비되지 않았을 때 게임 시작 실패 테스트")
    void startGame_NotAllMembersReady_ThrowsException() {
        // given
        Long quizRoomId = 1L;
        Long userId = 1L;
        User roomOwner = User.builder().userId(userId).build();
        QuizRoom quizRoom = QuizRoom.builder()
                .quizRoomId(quizRoomId)
                .roomOwner(roomOwner)
                .roomStatus(RoomStatus.W)
                .build();

        List<RoomMember> members = Collections.singletonList(RoomMember.builder().isReady(Status.N).build());

        when(quizRoomRepository.findById(quizRoomId)).thenReturn(Optional.of(quizRoom));
        when(roomMemberRepository.findByQuizRoom(quizRoom)).thenReturn(members);

        // when & then
        assertThrows(BusinessException.class, () -> {
            quizRoomCommandService.startGame(quizRoomId, userId);
        });
    }

    @Test
    @DisplayName("문제가 충분하지 않을 때 게임 시작 실패 테스트")
    void startGame_InsufficientQuestions_ThrowsException() {
        // given
        Long quizRoomId = 1L;
        Long userId = 1L;
        User roomOwner = User.builder().userId(userId).build();
        Category category = Category.builder().categoryId(1L).build();
        QuizRoom quizRoom = QuizRoom.builder()
                .quizRoomId(quizRoomId)
                .roomOwner(roomOwner)
                .category(category)
                .roomStatus(RoomStatus.W)
                .roomQuizLimit(10) // Requires 10 questions
                .build();

        List<RoomMember> members = Collections.singletonList(RoomMember.builder().isReady(Status.Y).build());
        List<Question> questions = Collections.emptyList(); // Not enough questions

        when(quizRoomRepository.findById(quizRoomId)).thenReturn(Optional.of(quizRoom));
        when(roomMemberRepository.findByQuizRoom(quizRoom)).thenReturn(members);
        when(jpaQuestionRepository.findByCategoryAndIsQualifiedAndIsDeleted(any(), any(), any())).thenReturn(questions);

        // when & then
        assertThrows(BusinessException.class, () -> {
            quizRoomCommandService.startGame(quizRoomId, userId);
        });
    }

    @Test
    @DisplayName("다음 문제로 진행 성공 테스트")
    void advanceNextQuestion_Success() {
        // given
        Long quizRoomId = 1L;
        QuizRoom quizRoom = QuizRoom.builder()
                .quizRoomId(quizRoomId)
                .roomStatus(RoomStatus.P)
                .roomQuizLimit(10)
                .build();
        QuizCurrentIndex quizIndex = QuizCurrentIndex.builder().currentQuizIndex(1).build();

        when(quizRoomRepository.findById(quizRoomId)).thenReturn(Optional.of(quizRoom));
        when(quizCurrentIndexRepository.findByQuizRoom(quizRoom)).thenReturn(Optional.of(quizIndex));
        when(roomQuestionRepository.findByQuizRoomAndQuizOrder(any(), anyInt())).thenReturn(Optional.of(new RoomQuestion()));
        when(roomMemberRepository.findByQuizRoom(quizRoom)).thenReturn(Collections.emptyList());

        // when
        quizRoomCommandService.advanceNextQuestion(quizRoomId);

        // then
        verify(quizCurrentIndexRepository).save(quizIndex);
        assertThat(quizIndex.getCurrentQuizIndex()).isEqualTo(2);
        verify(quizGameAdvanceScheduler).scheduleAdvanceQuestion(quizRoomId, 30 * 1000L);
    }

    @Test
    @DisplayName("마지막 문제에서 다음 문제 진행시 게임 종료 테스트")
    void advanceNextQuestion_LastQuestion_EndsGame() {
        // given
        Long quizRoomId = 1L;
        QuizRoom quizRoom = QuizRoom.builder()
                .quizRoomId(quizRoomId)
                .roomStatus(RoomStatus.P)
                .roomQuizLimit(10)
                .build();
        QuizCurrentIndex quizIndex = QuizCurrentIndex.builder().currentQuizIndex(10).build();

        when(quizRoomRepository.findById(quizRoomId)).thenReturn(Optional.of(quizRoom));
        when(quizCurrentIndexRepository.findByQuizRoom(quizRoom)).thenReturn(Optional.of(quizIndex));
        when(roomQuestionRepository.findByQuizRoomAndQuizOrder(any(), anyInt())).thenReturn(Optional.of(new RoomQuestion()));
        when(roomMemberRepository.findByQuizRoom(quizRoom)).thenReturn(Collections.emptyList());
        when(quizRoomQueryService.getResultsByRoom(anyLong(), anyInt(), anyInt())).thenReturn(Collections.emptyList());

        // when
        quizRoomCommandService.advanceNextQuestion(quizRoomId);

        // then
        assertThat(quizRoom.getRoomStatus()).isEqualTo(RoomStatus.E);
    }

    @Test
    @DisplayName("진행중이 아닌 게임에서 다음 문제 진행시 실패 테스트")
    void advanceNextQuestion_NotInPlay_ThrowsException() {
        // given
        Long quizRoomId = 1L;
        QuizRoom quizRoom = QuizRoom.builder()
                .quizRoomId(quizRoomId)
                .roomStatus(RoomStatus.W) // Not in play
                .build();

        when(quizRoomRepository.findById(quizRoomId)).thenReturn(Optional.of(quizRoom));

        // when & then
        assertThrows(BusinessException.class, () -> {
            quizRoomCommandService.advanceNextQuestion(quizRoomId);
        });
    }

    @Test
    @DisplayName("방 강제 종료 성공 테스트")
    void forceEndRoom_Success() {
        // given
        Long quizRoomId = 1L;
        QuizRoom quizRoom = QuizRoom.builder()
                .quizRoomId(quizRoomId)
                .build();

        when(quizRoomRepository.findById(quizRoomId)).thenReturn(Optional.of(quizRoom));
        when(quizRoomQueryService.getResultsByRoom(anyLong(), anyInt(), anyInt())).thenReturn(Collections.emptyList());

        // when
        quizRoomCommandService.forceEndRoom(quizRoomId);

        // then
        verify(quizRoomRepository).findById(quizRoomId);
        verify(quizRoomRepository).save(quizRoom);
        assertThat(quizRoom.getRoomStatus()).isEqualTo(RoomStatus.E);
    }
}