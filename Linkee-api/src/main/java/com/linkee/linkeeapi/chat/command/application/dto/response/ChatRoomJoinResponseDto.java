package com.linkee.linkeeapi.chat.command.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatRoomJoinResponseDto {
    private Long roomId;
    private String message; // 입장 성공 메시지
}