package com.linkee.linkeeapi.chat.command.application.service.chat_service;

import com.linkee.linkeeapi.chat.command.application.dto.request.ChatMessageRequestDto;
import com.linkee.linkeeapi.chat.command.application.dto.response.ChatMemberDto;
import com.linkee.linkeeapi.chat.command.domain.aggregate.entity.ChatMember;
import com.linkee.linkeeapi.chat.command.domain.aggregate.entity.ChatRoom;
import com.linkee.linkeeapi.chat.command.instructure.repository.ChatMemberRepository;
import com.linkee.linkeeapi.chat.command.instructure.repository.ChatRoomRepository;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.users.command.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomInOutService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final ChatRoomBroadcastService broadcastService;

    /* ------------------------------------------------------
     *  방 입장
     * ------------------------------------------------------ */
    @Transactional
    public void joinRoom(Long roomId, User user, Integer inputRoomCode) {

        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // 비밀방 비밀번호 체크
        if (Status.Y.equals(room.getIsPrivate())) {
            if (room.getRoomCode() == null || !room.getRoomCode().equals(inputRoomCode)) {
                throw new RuntimeException("비밀번호가 틀렸습니다.");
            }
        }

        boolean alreadyJoined = chatMemberRepository.existsByChatRoomAndUser(room, user);

        if (alreadyJoined) {
            ChatMember cm = chatMemberRepository.findByChatRoomAndUser(room, user)
                    .orElseThrow();
            cm.setJoinedAt(LocalDateTime.now());
            cm.modifyIsRead();
        } else {
            chatMemberRepository.save(ChatMember.builder()
                    .chatRoom(room)
                    .user(user)
                    .build());

            room.increaseJoinedCount();
            chatRoomRepository.save(room);
        }

        // 🔥 실시간 참여자 리스트 갱신
        broadcastService.broadcastMemberList(roomId);

        // 🔥 실시간 입장 메시지 보내기
        broadcastService.broadcastEnter(roomId, user.getUserNickname());
    }

    /* ------------------------------------------------------
     *  방 퇴장
     * ------------------------------------------------------ */
    @Transactional
    public void leaveRoom(Long roomId, User user) {

        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        boolean isOwner = room.getRoomOwner().getUserId().equals(user.getUserId());

        if (isOwner) {
            chatRoomRepository.delete(room);
            chatRoomRepository.flush();
        } else {
            ChatMember cm = chatMemberRepository.findByChatRoomAndUser(room, user)
                    .orElseThrow(() -> new RuntimeException("Member not found"));

            chatMemberRepository.delete(cm);
            room.decreaseJoinedCount();

            if (room.getJoinedCount() == 0) {
                chatRoomRepository.delete(room);
            } else {
                chatRoomRepository.save(room);
            }
        }

        // 🔥 실시간 참여자 리스트 갱신
        broadcastService.broadcastMemberList(roomId);

        // 🔥 실시간 퇴장 메시지
        broadcastService.broadcastLeave(roomId, user.getUserNickname());
    }

    /* ------------------------------------------------------
     *  참여자 리스트 조회
     * ------------------------------------------------------ */
    @Transactional(readOnly = true)
    public List<ChatMemberDto> getRoomMembers(Long roomId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        return chatMemberRepository.findAllByChatRoomAndLeftAtIsNull(room)
                .stream()
                .map(cm -> new ChatMemberDto(
                        cm.getUser().getUserId(),
                        cm.getUser().getUserNickname(),
                        cm.getJoinedAt()
                )).toList();
    }
}
