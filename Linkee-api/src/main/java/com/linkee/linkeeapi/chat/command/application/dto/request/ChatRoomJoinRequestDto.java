package com.linkee.linkeeapi.chat.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomJoinRequestDto {
    private Integer roomCode; // 비밀방 코드
}
