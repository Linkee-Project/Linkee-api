package com.linkee.linkeeapi.chat_message.command.application.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MarkAsReadRequest {
    private Long chatRoomId;
    private Long userId;
}
