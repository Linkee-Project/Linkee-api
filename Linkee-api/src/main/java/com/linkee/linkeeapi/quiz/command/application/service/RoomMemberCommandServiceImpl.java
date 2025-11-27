package com.linkee.linkeeapi.quiz.command.application.service;

import com.linkee.linkeeapi.common.enums.RoomStatus;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.quiz.command.domain.aggregate.QuizRoom;
import com.linkee.linkeeapi.quiz.command.infrastructure.repository.QuizRoomRepository;
import com.linkee.linkeeapi.quiz.command.application.dto.request.RoomMemberCreateRequest;
import com.linkee.linkeeapi.quiz.command.application.dto.response.RoomMemberCreateResponse;
import com.linkee.linkeeapi.quiz.command.domain.aggregate.RoomMember;
import com.linkee.linkeeapi.quiz.command.infrastructure.repository.RoomMemberRepository;
import com.linkee.linkeeapi.quiz.query.service.RoomMemberQueryService;
import com.linkee.linkeeapi.users.command.domain.entity.User;
import com.linkee.linkeeapi.users.command.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/*
 * RoomMemberCommandService 인터페이스의 구현체.
 * 룸 멤버 관련 비즈니스 로직을 실제로 처리합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RoomMemberCommandServiceImpl implements RoomMemberCommandService {

    private final RoomMemberRepository roomMemberRepository;
    private final UserRepository userRepository;
    private final QuizRoomRepository quizRoomRepository;
    private final QuizRoomWebSocketService quizRoomWebSocketService;
    private final RoomMemberQueryService roomMemberQueryService;

    /*
     * 새로운 룸 멤버를 생성하고 저장합니다.(입장)
     * 사용자 및 퀴즈룸 존재 여부를 확인 후 룸 멤버를 빌드하여 저장합니다.
     * @param request 생성할 룸 멤버의 정보를 담은 요청 객체
     * @throws IllegalArgumentException 사용자 또는 퀴즈룸을 찾을 수 없을 경우 발생
     */
    @Override
    public RoomMemberCreateResponse createRoomMember(RoomMemberCreateRequest request, Long userId) {
        //  1. 유저와 퀴즈방 조회
        QuizRoom quizRoom = quizRoomRepository.findById(request.getQuizRoomId())
                .orElseThrow(() -> new BusinessException(ErrorCode.QUIZ_ROOM_NOT_FOUND));

        // 비공개 방일 경우 코드 검증
        if (quizRoom.getIsPrivate() == Status.Y) {
            System.out.println("--- DEBUG ---");
            System.out.println("Request Code: '" + request.getRoomCode() + "'");
            System.out.println("DB Code     : '" + quizRoom.getRoomCode() + "'");
            System.out.println("Are they equal? " + java.util.Objects.equals(request.getRoomCode(), quizRoom.getRoomCode()));
            System.out.println("-------------");
            if (!java.util.Objects.equals(request.getRoomCode(), quizRoom.getRoomCode())) {
                throw new BusinessException(ErrorCode.INVALID_ROOM_CODE);
            }
        }

        // 유저 로드
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_USER_ID));

        //  대기중인 방인지 확인
        if (quizRoom.getRoomStatus() != RoomStatus.W) {
            throw new BusinessException(ErrorCode.QUIZ_ROOM_NOT_IN_WAITING_STATE);
        }
        //  방이 가득 찼는지 확인
        if (quizRoom.getJoinedCount() >= quizRoom.getRoomCapacity()) {
            throw new BusinessException(ErrorCode.QUIZ_ROOM_FULL);
        }
        //  이미 참여한 유저인지 확인(중복 방지)
        if (roomMemberRepository.existsByQuizRoomAndMember(quizRoom, user)) {
            throw new BusinessException(ErrorCode.USER_ALREADY_IN_ROOM);
        }

        RoomMember roomMember = RoomMember.builder()
                .member(user)
                .quizRoom(quizRoom)
                .isReady(user.getUserId()
                        .equals(quizRoom.getRoomOwner().getUserId()) ? Status.Y : Status.N) // 초기 상태는 '준비 안 됨' (N) 방장이면 자동으로 준비완료
                .joinedAt(LocalDateTime.now())
                .build();

        roomMemberRepository.save(roomMember);

        // 인원 수 재집계(감소 -1 대신 '남아있는 인원'을 다시 계산)
        Long roomId = roomMember.getQuizRoom().getQuizRoomId();
        int alive = roomMemberQueryService.countAliveMembers(roomId);
        roomMember.getQuizRoom().setJoinedCount(alive);

        // 멤버 목록 브로드캐스트
        quizRoomWebSocketService.broadcastMemberList(quizRoom.getQuizRoomId(), false);

        return RoomMemberCreateResponse.builder()
                .quizRoomId(quizRoom.getQuizRoomId())
                .quizRoomName(quizRoom.getRoomTitle())
                .userNickName(user.getUserNickname())
                .build();
    }

    /*
     * 특정 룸 멤버의 준비 상태(isReady)를 토글합니다. (Y <-> N)
     * @param roomMemberId 준비 상태를 변경할 룸 멤버의 ID
     */
    @Override
    @Transactional
    public void toggleReady(Long roomMemberId) {
        RoomMember roomMember = roomMemberRepository.findById(roomMemberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_USER_ID));
        if (roomMember.getLeftedAt() != null) return;

        //  1. 게임 상태 검증
        QuizRoom quizRoom = roomMember.getQuizRoom();
        System.out.println("--- toggleReady DEBUG ---");
        System.out.println("Room ID: " + quizRoom.getQuizRoomId());
        System.out.println("Room Status: " + quizRoom.getRoomStatus());
        System.out.println("-------------------------");
        if (quizRoom.getRoomStatus() != RoomStatus.W) {  // 대기(W) 상태가 아닐 경우
            throw new BusinessException(ErrorCode.QUIZ_ROOM_NOT_WAITING);
        }

        //  2. 준비 상태 토글
        if (roomMember.getIsReady() == Status.Y) {
            roomMember.setIsReady(Status.N);
        } else {
            roomMember.setIsReady(Status.Y);
        }

        Long roomId = quizRoom.getQuizRoomId();
        quizRoomWebSocketService.broadcastMemberList(roomId, false);
    }

    @Transactional
    public void selfLeaveRoom(Long roomMemberId,Long userId) {
        leave(roomMemberId, false); // 자발적 나감
    }

    /*
     * 방장이 특정 룸 멤버를 강제로 내보낸 시간을 기록합니다. (강퇴)
     * @param roomMemberId 강퇴할 룸 멤버의 ID
     */
    @Transactional
    public void kickRoomMember(Long roomMemberId, Long currentUserId) {
        RoomMember roomMember = roomMemberRepository.findById(roomMemberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_MEMBER_NOT_FOUND));

        Long ownerId = roomMember.getQuizRoom().getRoomOwner().getUserId();
        if (!ownerId.equals(currentUserId))
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "방장만 강퇴할 수 있습니다.");
        if (roomMember.getMember().getUserId().equals(currentUserId))
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "방장은 자신을 강퇴할 수 없습니다.");

        leave(roomMemberId, true);
    }

    private void leave(Long roomMemberId, boolean kicked) {
        RoomMember roomMember = roomMemberRepository.findById(roomMemberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_USER_ID));

        // 이미 나간 유저가 또 나가기 요청 해도 무시
        if (roomMember.getLeftedAt() != null) {
            return;
        }

        // 1) 준비상태 초기화 + 퇴장 시간 기록
        roomMember.setIsReady(Status.valueOf("N"));
        roomMember.setLeftedAt(LocalDateTime.now());
        // (옵션) m.setKicked(kicked ? "Y" : "N");

        // 2) 인원 수 재집계(감소 -1 대신 '남아있는 인원'을 다시 계산)
        Long roomId = roomMember.getQuizRoom().getQuizRoomId();
        int alive = roomMemberQueryService.countAliveMembers(roomId);
        roomMember.getQuizRoom().setJoinedCount(alive);

        // 3) 변경된 내용 브로드캐스트
        quizRoomWebSocketService.broadcastMemberList(roomId, kicked);
    }
}