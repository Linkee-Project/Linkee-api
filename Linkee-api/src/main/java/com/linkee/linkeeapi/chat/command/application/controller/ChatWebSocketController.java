package com.linkee.linkeeapi.chat.command.application.controller;

import com.linkee.linkeeapi.chat.command.application.dto.request.ChatMessageRequestDto;
import com.linkee.linkeeapi.chat.command.application.service.chat_service.ChatRoomInOutService;
import com.linkee.linkeeapi.chat.command.domain.aggregate.entity.ChatMessageMongo;
import com.linkee.linkeeapi.chat.command.instructure.repository.ChatMessageMongoRepository;
import com.linkee.linkeeapi.common.enums.ChatMessageType;
import com.linkee.linkeeapi.users.command.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatMessageMongoRepository chatMessageMongoRepository;
    private final ChatRoomInOutService chatRoomInOutService;
    private final SimpMessagingTemplate messagingTemplate;

    /* -----------------------------------------------------
     *  일반 채팅 메시지
     * ----------------------------------------------------- */
    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessageRequestDto dto,
                            SimpMessageHeaderAccessor accessor) {

        User sender = (User) accessor.getSessionAttributes().get("user");
        if (sender == null) throw new RuntimeException("Unauthorized");

        dto.setSenderId(sender.getUserId());
        dto.setSenderNickname(sender.getUserNickname());
        dto.setSentAt(LocalDateTime.now());
        dto.setType(ChatMessageType.MESSAGE);

        // Mongo 저장
        chatMessageMongoRepository.save(ChatMessageMongo.builder()
                .roomId(dto.getRoomId())
                .senderId(sender.getUserId())
                .senderNickname(sender.getUserNickname())
                .message(dto.getMessage())
                .sentAt(dto.getSentAt())
                .type(ChatMessageType.MESSAGE)
                .build()
        );

        // 실시간 브로드캐스트
        messagingTemplate.convertAndSend("/topic/chatroom/" + dto.getRoomId(), dto);
    }

    /* -----------------------------------------------------
     *  방 입장
     * ----------------------------------------------------- */
    @MessageMapping("/chat.join")
    public void joinRoom(SimpMessageHeaderAccessor accessor,
                         @Header("roomId") Long roomId,
                         @Header(value = "roomCode", required = false) Integer roomCode) {

        User user = (User) accessor.getSessionAttributes().get("user");
        if (user == null) throw new RuntimeException("Unauthorized");

        // 🔥 서비스가 직접 브로드캐스트 해줌
        chatRoomInOutService.joinRoom(roomId, user, roomCode);
    }

    /* -----------------------------------------------------
     *  방 퇴장
     * ----------------------------------------------------- */
    @MessageMapping("/chat.leave")
    public void leaveRoom(SimpMessageHeaderAccessor accessor,
                          @Header("roomId") Long roomId) {

        User user = (User) accessor.getSessionAttributes().get("user");
        if (user == null) throw new RuntimeException("Unauthorized");

        // 🔥 서비스가 직접 브로드캐스트 해줌
        chatRoomInOutService.leaveRoom(roomId, user);
    }
}
