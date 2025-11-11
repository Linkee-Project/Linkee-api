package com.linkee.linkeeapi.chat.command.application.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ChatMemberCreateRequest {
    private Long chatRoomId;
    private Long userId;
}
