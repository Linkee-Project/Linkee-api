package com.linkee.linkeeapi.chat.command.application.dto.request;

import com.linkee.linkeeapi.common.enums.ChatMessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageRequestDto {
    private ChatMessageType type;
    private Long roomId;
    private String message;
    private Long senderId; // JWT에서 가져올 ID
    private String senderNickname; // JWT에서 가져올 닉네임
    private LocalDateTime sentAt;
}