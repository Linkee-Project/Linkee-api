package com.linkee.linkeeapi.room_member.command.application.service;

import com.linkee.linkeeapi.common.enums.RoomStatus;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.quiz_room.command.domain.aggregate.QuizRoom;
import com.linkee.linkeeapi.quiz_room.command.infrastructure.repository.QuizRoomRepository;
import com.linkee.linkeeapi.room_member.command.application.dto.request.RoomMemberCreateRequest;
import com.linkee.linkeeapi.room_member.command.application.dto.response.RoomMemberCreateResponse;
import com.linkee.linkeeapi.room_member.command.domain.aggregate.RoomMember;
import com.linkee.linkeeapi.room_member.command.infrastructure.repository.RoomMemberRepository;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user.command.infrastructure.repository.UserRepository;
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

    /*
     * 새로운 룸 멤버를 생성하고 저장합니다.(입장)
     * 사용자 및 퀴즈룸 존재 여부를 확인 후 룸 멤버를 빌드하여 저장합니다.
     * @param request 생성할 룸 멤버의 정보를 담은 요청 객체
     * @throws IllegalArgumentException 사용자 또는 퀴즈룸을 찾을 수 없을 경우 발생
     */
    @Override
    public RoomMemberCreateResponse createRoomMember(RoomMemberCreateRequest request) {
        //  1. 유저와 퀴즈방 조회
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_USER_ID));
        QuizRoom quizRoom = quizRoomRepository.findById(request.getQuizRoomId())
                .orElseThrow(() ->  new BusinessException(ErrorCode.QUIZ_ROOM_NOT_FOUND));

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
                .isReady(Status.N) // 초기 상태는 '준비 안 됨' (N)
                .joinedAt(LocalDateTime.now())
                .build();

        roomMemberRepository.save(roomMember);

        //  퀴즈방의 현재 인원 수 1 증가
        quizRoom.setJoinedCount(quizRoom.getJoinedCount() + 1);
        quizRoomRepository.save(quizRoom);

        return RoomMemberCreateResponse.builder()
                .quizRoomId(quizRoom.getQuizRoomId())
                .quizRoomName(quizRoom.getRoomTitle())
                .userNickName(user.getUserNickname())
                .build();
    }

    /*
     * 특정 룸 멤버의 준비 상태(isReady)를 토글합니다. (Y <-> N)
     * @param roomMemberId 준비 상태를 변경할 룸 멤버의 ID
     * @throws IllegalArgumentException 룸 멤버를 찾을 수 없을 경우 발생
     */
    @Override
    @Transactional
    public void toggleReady(Long roomMemberId) {
        RoomMember roomMember = roomMemberRepository.findById(roomMemberId)
                .orElseThrow(() -> new IllegalArgumentException("RoomMember not found"));

        //  1. 게임 상태 검증
        QuizRoom quizRoom = roomMember.getQuizRoom();
        if(quizRoom.getRoomStatus() != RoomStatus.W) {  // 대기(W) 상태가 아닐 경우
            throw new BusinessException(ErrorCode.QUIZ_ROOM_NOT_WAITING);
        }

        //  2. 준비 상태 토글
        if (roomMember.getIsReady() == Status.Y) {
            roomMember.setIsReady(Status.N);
        } else {
            roomMember.setIsReady(Status.Y);
        }
    }


    /*
     * 특정 룸 멤버가 스스로 방을 나간 시간을 기록합니다. (자발적 나감)
     * @param roomMemberId 방을 나갈 룸 멤버의 ID
     * @throws IllegalArgumentException 룸 멤버를 찾을 수 없을 경우 발생
     */
    @Override
    @Transactional
    public void selfLeaveRoom(Long roomMemberId) {
        RoomMember roomMember = roomMemberRepository.findById(roomMemberId)
                .orElseThrow(() -> new IllegalArgumentException("RoomMember not found"));
        roomMember.setLeftedAt(LocalDateTime.now());
    }

    /*
     * 방장이 특정 룸 멤버를 강제로 내보낸 시간을 기록합니다. (강퇴)
     * @param roomMemberId 강퇴할 룸 멤버의 ID
     * @throws IllegalArgumentException 룸 멤버를 찾을 수 없을 경우 발생
     */
    @Override
    @Transactional
    public void kickRoomMember(Long roomMemberId) {
        RoomMember roomMember = roomMemberRepository.findById(roomMemberId)
                .orElseThrow(() -> new IllegalArgumentException("RoomMember not found"));
        roomMember.setLeftedAt(LocalDateTime.now());
    }
}