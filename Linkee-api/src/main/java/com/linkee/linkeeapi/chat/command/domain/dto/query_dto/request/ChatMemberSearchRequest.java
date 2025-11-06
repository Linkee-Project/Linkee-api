package com.linkee.linkeeapi.chat.command.domain.dto.query_dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMemberSearchRequest {

    private String keyword;
    private Integer page;
    private Integer size;
    private Integer offset;
    private Long chatRoomId;
    private Long userId;
}
