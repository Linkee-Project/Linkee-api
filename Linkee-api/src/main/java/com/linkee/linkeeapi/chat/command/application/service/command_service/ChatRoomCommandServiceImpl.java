package com.linkee.linkeeapi.chat.command.application.service.command_service;

import com.linkee.linkeeapi.chat.chat_command.chat_domain.entity.ChatMember;
import com.linkee.linkeeapi.chat.chat_command.chat_repository.ChatMemberRepository;
import com.linkee.linkeeapi.chat.command.domain.dto.command_dto.request.ChatRoomCreateRequestDto;
import com.linkee.linkeeapi.chat.command.domain.dto.command_dto.request.ChatRoomDeleteRequestDto;
import com.linkee.linkeeapi.chat.chat_command.chat_domain.entity.ChatRoom;
import com.linkee.linkeeapi.chat.chat_command.chat_domain.entity.ChatRoomType;
import com.linkee.linkeeapi.chat.chat_command.chat_repository.ChatRoomRepository;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user.command.application.service.util.UserFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomCommandServiceImpl implements ChatRoomCommandService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserFinder userFinder;
    private final ChatMemberRepository chatMemberRepository;
    private ChatRoomCreateRequestDto request;
    private User user;

    /*---------------------------------------create------------------------------------------*/

    //자율방 or 채팅방 만들기
    /* 채팅방
    *   공개여부 "N"
    * */

    /*자율방
        방인원수 설정 (최대 5명)
        공개여부 "Y"
            -> roomcode 작성
    */

    // ✅ 채팅방/게임방 생성
    @Transactional
    public void createRoom(ChatRoomCreateRequestDto request ,User user) {
        this.request = request;
        this.user = user;
        /* 기본값 설정*/
        ChatRoomType roomType = request.getChatRoomType();
        Status isPrivate;

        // 채팅방이면 무조건 공개, 게임방이면 요청값 사용
        if (roomType == ChatRoomType.CHAT) {
            isPrivate = Status.N;
        } else {
            isPrivate = Optional.ofNullable(request.getIsPrivate()).orElse(Status.N);
        }

        Integer roomCode = request.getRoomCode();
        Integer capacity = request.getRoomCapacity();

        if (request.getChatRoomName().trim().isEmpty()) {
            throw new IllegalArgumentException("채팅방 이름은 비워둘 수 없습니다.");
        }

        Integer validatedRoomCode = validateRoomCode(isPrivate, roomCode);

        if (roomType == ChatRoomType.GAME) {
            capacity = validateGameRoomCapacity(capacity);
        } else {
            capacity = null; // 채팅방은 인원수 제한 없음
        }

        ChatRoom chatRoom = ChatRoom.builder()
                .chatRoomName(request.getChatRoomName())
                .chatRoomType(roomType)
                .isPrivate(isPrivate)
                .roomCode(validatedRoomCode)
                .roomOwner(user)
                .roomCapacity(capacity)
                .roomStatus(Status.Y)
                .joinedCount(1)
                .build();

        chatRoomRepository.save(chatRoom);
    }


    public void enterRoom(Long roomId, User user) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

        boolean alreadyJoined = chatMemberRepository.existsByChatRoomAndUser(room, user);
        if (alreadyJoined) return;

        if (room.getRoomCapacity() != null && room.getJoinedCount() >= room.getRoomCapacity()) {
            throw new IllegalStateException("방 인원이 가득 찼습니다.");
        }

        ChatMember member = ChatMember.builder()
                .chatRoom(room)
                .user(user)
                .build();

        chatMemberRepository.save(member);

        room.increaseJoinedCount();
        chatRoomRepository.save(room);
    }


    //roomcode 검증
    private Integer validateRoomCode(Status isPrivate, Integer roomCode) {
        if (isPrivate == Status.Y) {
            // 비공개방인데 코드가 없으면 오류
            if (roomCode == null || roomCode == 0) {
                throw new IllegalArgumentException("비공개 방(Status.Y)은 roomCode를 반드시 입력해야 합니다.");
            }
            return roomCode;
        } else {
            // 공개방이면 코드값은 무조건 0으로 설정
            return 0;
        }
    }

    // GAME 방 인원수 검증
    private Integer validateGameRoomCapacity(Integer capacity) {
        if (capacity == null || capacity == 0) {
            throw new IllegalArgumentException("게임방은 최소 2명 이상의 인원이 필요합니다.");
        }
        if (capacity < 2) return 2;
        if (capacity > 5) return 5;
        return capacity;
    }

    /*---------------------------------------delete------------------------------------------*/
    //게임방 방장 나가면 room_status = 'N'
    //채팅방 방 인원이 0명이 되면 room_status = 'N'

    /*게임방일 경우:
    방장(roomOwner)이 leaveRoom을 호출하면 room_status = N으로 바꿔 종료.
    다른 멤버가 나가는 건 인원수만 감소.
    채팅방일 경우:
    멤버가 나가면 joinedCount 감소.
    joinedCount가 0이면 room_status = N으로 바꿈.*/

    @Override
    @Transactional
    public void deleteGameRoom(ChatRoomDeleteRequestDto request) {
        ChatRoom chatRoom = chatRoomRepository.findById(request.getChatRoomId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

        if (!chatRoom.getRoomOwner().getUserId().equals(request.getUserId())) {
            throw new IllegalArgumentException("게임방에서는 방장만 방을 종료할 수 있습니다.");
        }

        // 방장 나감 -> 게임방 종료
        chatRoom.closeRoom();
        chatRoomRepository.save(chatRoom);

        //모든 멤버에 leftAt 기록 남기기
        List<ChatMember> members = chatMemberRepository.findByChatRoom(chatRoom);
        for (ChatMember member : members) {
            if(member.getLeftAt() == null) {
                member.modifyLeftAt();
                chatMemberRepository.save(member);
            }
        }
    }

    @Override
    @Transactional
    public void leaveRoom(ChatRoomDeleteRequestDto request) {
        ChatRoom chatRoom = chatRoomRepository.findById(request.getChatRoomId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

        User user = userFinder.getById(request.getUserId());

        ChatMember chatMember = chatMemberRepository.findByChatRoomAndUser(chatRoom, user)
                        .orElseThrow(() -> new IllegalArgumentException("해당 사용자는 방 멤버가 아닙니다"));

        //나간시간 기록
        chatMember.modifyLeftAt();
        chatMemberRepository.save(chatMember);

        //인원 감소
        chatRoom.decreaseJoinedCount();
        chatRoomRepository.save(chatRoom);
    }

}
