package com.linkee.linkeeapi.chat.command.domain.dto.command_dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageCreateRequest {

    private String messageContent;
    private Long chatRoomId;
    private Long senderId;

}
