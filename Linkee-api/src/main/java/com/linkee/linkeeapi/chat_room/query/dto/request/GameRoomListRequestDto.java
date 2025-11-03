package com.linkee.linkeeapi.chat_room.query.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GameRoomListRequestDto {

    private Integer page;
    private Integer size;
}
