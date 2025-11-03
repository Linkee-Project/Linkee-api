package com.linkee.linkeeapi.chat_member.command.application.dto.reqeust;

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
