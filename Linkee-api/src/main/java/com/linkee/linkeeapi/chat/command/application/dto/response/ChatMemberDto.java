package com.linkee.linkeeapi.chat.command.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ChatMemberDto {
    private Long userId;
    private String userNickname;
    private LocalDateTime joinedAt;
}
