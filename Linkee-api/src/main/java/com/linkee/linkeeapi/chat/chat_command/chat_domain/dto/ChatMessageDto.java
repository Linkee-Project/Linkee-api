package com.linkee.linkeeapi.chat.chat_command.chat_domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDto {
    private Long roomId;
    private String message;
    private Long senderId; // JWT에서 가져올 ID
    private String senderNickname; // JWT에서 가져올 닉네임
    private LocalDateTime sentAt;
}