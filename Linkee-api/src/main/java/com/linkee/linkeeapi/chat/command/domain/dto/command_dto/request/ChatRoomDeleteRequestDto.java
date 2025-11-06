package com.linkee.linkeeapi.chat.command.domain.dto.command_dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDeleteRequestDto {

    private Long chatRoomId;
    private Long userId;
}
