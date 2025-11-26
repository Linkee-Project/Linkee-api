package com.linkee.linkeeapi.chat.command.application.controller;

import com.linkee.linkeeapi.chat.command.application.dto.request.ChatMessageRequestDto;
import com.linkee.linkeeapi.chat.command.application.service.chat_service.ChatRoomInOutService;
import com.linkee.linkeeapi.chat.command.domain.aggregate.entity.ChatMessageMongo;
import com.linkee.linkeeapi.chat.command.instructure.repository.ChatMessageMongoRepository;
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
     *  메시지 전송
     * ----------------------------------------------------- */
    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessageRequestDto messageDto,
                            SimpMessageHeaderAccessor accessor) {

        User sender = (User) accessor.getSessionAttributes().get("user");
        if (sender == null) throw new RuntimeException("Unauthorized");

        // 메시지 구성
        messageDto.setSenderId(sender.getUserId());
        messageDto.setSenderNickname(sender.getUserNickname());
        messageDto.setSentAt(LocalDateTime.now());

        // Mongo 저장
        chatMessageMongoRepository.save(ChatMessageMongo.builder()
                .roomId(messageDto.getRoomId())
                .senderId(sender.getUserId())
                .senderNickname(sender.getUserNickname())
                .message(messageDto.getMessage())
                .sentAt(messageDto.getSentAt())
                .build()
        );

        // 메시지 전송
        messagingTemplate.convertAndSend(
                "/topic/chatroom/" + messageDto.getRoomId(),
                messageDto
        );
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

        // 서비스 호출 → 메시지 DTO 리턴
        ChatMessageRequestDto joinMsg =
                chatRoomInOutService.joinRoom(roomId, user.getUserEmail(), roomCode);

        messagingTemplate.convertAndSend("/topic/chatroom/" + roomId, joinMsg);
    }


    /* -----------------------------------------------------
     *  방 퇴장
     * ----------------------------------------------------- */
    @MessageMapping("/chat.leave")
    public void leaveRoom(SimpMessageHeaderAccessor accessor,
                          @Header("roomId") Long roomId) {

        User user = (User) accessor.getSessionAttributes().get("user");
        if (user == null) throw new RuntimeException("Unauthorized");

        ChatMessageRequestDto leaveMsg =
                chatRoomInOutService.leaveRoom(roomId, user.getUserEmail());

        messagingTemplate.convertAndSend("/topic/chatroom/" + roomId, leaveMsg);
    }
}
