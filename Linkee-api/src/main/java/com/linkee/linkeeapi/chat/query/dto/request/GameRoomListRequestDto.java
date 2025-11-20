package com.linkee.linkeeapi.chat.query.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameRoomListRequestDto {

    private Integer page;
    private Integer size;
}
