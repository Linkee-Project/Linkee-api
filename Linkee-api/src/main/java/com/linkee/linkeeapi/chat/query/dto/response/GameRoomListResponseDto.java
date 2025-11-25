package com.linkee.linkeeapi.chat.query.dto.response;

import com.linkee.linkeeapi.common.enums.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameRoomListResponseDto {
    private Long chatRoomId;
    private String chatRoomName;
    private Status isPrivate;
    private Long ownerId;
    private Integer joinedCount;
    private Integer roomCapacity;
}
