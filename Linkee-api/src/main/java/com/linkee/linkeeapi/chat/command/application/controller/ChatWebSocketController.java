package com.linkee.linkeeapi.chat.command.application.controller;



import com.linkee.linkeeapi.chat.command.application.dto.request.ChatMessageRequestDto;
import com.linkee.linkeeapi.chat.command.application.dto.response.ChatMemberDto;
import com.linkee.linkeeapi.chat.command.application.service.chat_service.ChatRoomInOutService;
import com.linkee.linkeeapi.chat.command.domain.aggregate.entity.ChatMember;
import com.linkee.linkeeapi.chat.command.domain.aggregate.entity.ChatMessageMongo;
import com.linkee.linkeeapi.chat.command.instructure.repository.ChatMessageMongoRepository;
import com.linkee.linkeeapi.common.config.jwt.JwtTokenProvider;
import com.linkee.linkeeapi.users.command.domain.entity.User;
import com.linkee.linkeeapi.users.command.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatMessageMongoRepository chatMessageMongoRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ChatRoomInOutService chatRoomInOutService;
    private final SimpMessagingTemplate messagingTemplate;

    // 메시지 전송
    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessageRequestDto messageDto,
                            @Header("Authorization") String token) {

        User sender = validateTokenAndGetUser(token);

        messageDto.setSenderId(sender.getUserId());
        messageDto.setSenderNickname(sender.getUserNickname());
        messageDto.setSentAt(LocalDateTime.now());

        chatMessageMongoRepository.save(ChatMessageMongo.builder()
                .roomId(messageDto.getRoomId())
                .senderId(sender.getUserId())
                .senderNickname(sender.getUserNickname())
                .message(messageDto.getMessage())
                .sentAt(messageDto.getSentAt())
                .build());

        // 방별 메시지 전송
        messagingTemplate.convertAndSend(
                "/topic/chatroom/" + messageDto.getRoomId(),
                messageDto
        );
    }


    // 방 입장
    @MessageMapping("/chat.join")
    public void joinRoom(@Header("roomId") Long roomId,
                         @Header("Authorization") String token,
                         @Header(value = "roomCode", required = false) Integer roomCode) {

        ChatMessageRequestDto joinMessage = chatRoomInOutService.joinRoom(roomId, token, roomCode);

        // 입장 메시지 전송
        messagingTemplate.convertAndSend("/topic/chatroom/" + roomId, joinMessage);


    }

    // 방 퇴장
    @MessageMapping("/chat.leave")
    public void leaveRoom(@Header("roomId") Long roomId,
                          @Header("Authorization") String token) {

        ChatMessageRequestDto leaveMessage = chatRoomInOutService.leaveRoom(roomId, token);

        // 퇴장 메시지 전송
        messagingTemplate.convertAndSend("/topic/chatroom/" + roomId, leaveMessage);


    }




    private User validateTokenAndGetUser(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new RuntimeException("Unauthorized");
        }
        String userEmail = jwtTokenProvider.getUsername(token);
        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}