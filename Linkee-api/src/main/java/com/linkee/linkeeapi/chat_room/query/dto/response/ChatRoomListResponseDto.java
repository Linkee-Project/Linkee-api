package com.linkee.linkeeapi.chat_room.query.dto.response;

import com.linkee.linkeeapi.common.enums.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomListResponseDto {

    private String chatRoomName;
    private Long ownerId;
    private Integer joinedCount;
}
