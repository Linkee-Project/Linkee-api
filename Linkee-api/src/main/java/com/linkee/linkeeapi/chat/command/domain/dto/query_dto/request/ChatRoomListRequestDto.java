package com.linkee.linkeeapi.chat.command.domain.dto.query_dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomListRequestDto {

    private Long userId;
    private Integer page;
    private Integer size;
}
