package com.linkee.linkeeapi.chat_room.command.application.dto.request;

import jakarta.validation.constraints.NotNull;
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
