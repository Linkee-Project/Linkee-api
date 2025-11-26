package com.linkee.linkeeapi.chat.command.application.service.chat_service;

import com.linkee.linkeeapi.chat.command.application.dto.request.ChatMessageRequestDto;
import com.linkee.linkeeapi.chat.command.application.dto.response.ChatMemberDto;
import com.linkee.linkeeapi.chat.command.domain.aggregate.entity.ChatRoom;
import com.linkee.linkeeapi.chat.command.instructure.repository.ChatMemberRepository;
import com.linkee.linkeeapi.chat.command.instructure.repository.ChatRoomRepository;
import com.linkee.linkeeapi.common.enums.ChatMessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomBroadcastService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMemberRepository chatMemberRepository;
    private final ChatRoomRepository chatRoomRepository;

    // 참여자 목록
    @Transactional(readOnly = true)
    public void broadcastMemberList(Long roomId) {
        chatRoomRepository.findById(roomId).ifPresent(room -> {
            List<ChatMemberDto> members = chatMemberRepository
                    .findAllByChatRoomAndLeftAtIsNull(room)
                    .stream()
                    .map(cm -> new ChatMemberDto(
                            cm.getUser().getUserId(),
                            cm.getUser().getUserNickname(),
                            cm.getJoinedAt()))
                    .toList();

            messagingTemplate.convertAndSend(
                    "/topic/chatroom/" + roomId + "/members", members
            );
        });
    }

   // 입장
    public void broadcastEnter(Long roomId, String nickname) {
        ChatMessageRequestDto msg = ChatMessageRequestDto.builder()
                .type(ChatMessageType.ENTER)
                .roomId(roomId)
                .senderNickname(nickname)
                .message(nickname + "님이 입장했습니다.")
                .sentAt(LocalDateTime.now())
                .build();

        messagingTemplate.convertAndSend("/topic/chatroom/" + roomId, msg);
    }

    //퇴장
    public void broadcastLeave(Long roomId, String nickname) {
        ChatMessageRequestDto msg = ChatMessageRequestDto.builder()
                .type(ChatMessageType.LEAVE)
                .roomId(roomId)
                .senderNickname(nickname)
                .message(nickname + "님이 퇴장했습니다.")
                .sentAt(LocalDateTime.now())
                .build();

        messagingTemplate.convertAndSend("/topic/chatroom/" + roomId, msg);
    }
}
