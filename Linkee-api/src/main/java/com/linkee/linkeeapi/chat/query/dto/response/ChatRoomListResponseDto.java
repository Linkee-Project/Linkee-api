package com.linkee.linkeeapi.chat.query.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatRoomListResponseDto {

    private Long chatRoomId;
    private String chatRoomName;
    private Long ownerId;
    private Integer joinedCount;
}
