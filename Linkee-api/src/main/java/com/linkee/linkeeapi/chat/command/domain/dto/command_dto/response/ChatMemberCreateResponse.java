package com.linkee.linkeeapi.chat.command.domain.dto.command_dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ChatMemberCreateResponse {
    private Long chatRoomId;
    private String chatRoomName;
    private String userNickName;
}
