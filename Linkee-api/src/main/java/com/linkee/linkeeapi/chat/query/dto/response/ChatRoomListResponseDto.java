package com.linkee.linkeeapi.chat.query.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomListResponseDto {

    private String chatRoomName;
    private Long ownerId;
    private Integer joinedCount;
}
