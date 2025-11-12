package com.linkee.linkeeapi.chat.command.application.service.chat_service;

import com.linkee.linkeeapi.chat.command.application.dto.response.ChatMemberDto;
import com.linkee.linkeeapi.chat.command.domain.aggregate.entity.ChatRoom;
import com.linkee.linkeeapi.chat.command.instructure.repository.ChatMemberRepository;
import com.linkee.linkeeapi.chat.command.instructure.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomBroadcastService {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMemberRepository chatMemberRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional(readOnly = true)
    public void broadcastMemberList(Long roomId) {
        chatRoomRepository.findById(roomId).ifPresent(room -> {
            List<ChatMemberDto> members = chatMemberRepository.findAllByChatRoomAndLeftAtIsNull(room)
                    .stream()
                    .map(cm -> new ChatMemberDto(cm.getUser().getUserId(), cm.getUser().getUserNickname(), cm.getJoinedAt()))
                    .toList();

            messagingTemplate.convertAndSend("/topic/chatroom/" + roomId + "/members", members);
        });
    }
}