package com.linkee.linkeeapi.quiz_room.command.application.service;

import com.linkee.linkeeapi.category.command.aggregate.Category;
import com.linkee.linkeeapi.category.command.infrastructure.repository.CategoryRepository;
import com.linkee.linkeeapi.common.enums.RoomStatus;
import com.linkee.linkeeapi.quiz_room.command.application.dto.request.QuizRoomCreateRequestDto;
import com.linkee.linkeeapi.quiz_room.command.application.dto.request.QuizRoomDeleteRequestDto;
import com.linkee.linkeeapi.quiz_room.command.domain.aggregate.QuizRoom;
import com.linkee.linkeeapi.quiz_room.command.infrastructure.repository.QuizRoomRepository;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user.command.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
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
    private final UserRepository userRepository;

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
        User roomOwner = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        // 2. 요청된 카테고리 ID에 해당하는 카테고리 정보를 조회합니다.
        // 카테고리가 존재하지 않으면 예외를 발생시킵니다.
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + request.getCategoryId()));

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

    @Override
    @Transactional
    public void deleteQuizRoom(QuizRoomDeleteRequestDto request) {
        QuizRoom quizRoom = quizRoomRepository.findById(request.getQuizRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Quiz room not found with ID: " + request.getQuizRoomId()));

        if (!quizRoom.getRoomOwner().getUserId().equals(request.getUserId())) {
            throw new IllegalArgumentException("방장만 방을 삭제할 수 있습니다.");
        }

        quizRoom.setRoomStatus(RoomStatus.E);
        quizRoom.setEndedAt(LocalDateTime.now());
        quizRoomRepository.save(quizRoom);
    }

    @Override
    @Transactional
    public void leaveQuizRoom(QuizRoomDeleteRequestDto request) {
        QuizRoom quizRoom = quizRoomRepository.findById(request.getQuizRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Quiz room not found with ID: " + request.getQuizRoomId()));

        if (quizRoom.getRoomStatus() == RoomStatus.P) {
            throw new IllegalStateException("게임 중에는 방을 나갈 수 없습니다.");
        }

        if (quizRoom.getRoomOwner().getUserId().equals(request.getUserId())) {
            throw new IllegalArgumentException("방장은 방을 나갈 수 없습니다. 방 삭제 기능을 이용해주세요.");
        }

        quizRoom.setJoinedCount(quizRoom.getJoinedCount() - 1);
        quizRoomRepository.save(quizRoom);
    }
}
