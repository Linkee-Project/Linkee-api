package com.linkee.linkeeapi.chat.command.domain.dto.command_dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatMemberDeleteResponse {
    private Long chatRoomId;
    private String userNickName;
}
