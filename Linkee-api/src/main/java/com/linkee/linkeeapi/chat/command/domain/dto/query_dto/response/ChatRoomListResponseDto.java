package com.linkee.linkeeapi.chat.command.domain.dto.query_dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomListResponseDto {

    private String chatRoomName;
    private Long ownerId;
    private Integer joinedCount;
}
