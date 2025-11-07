package com.linkee.linkeeapi.chat.command.domain.dto.query_dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageResponse {

    private Long chatMessageId;
    private String MessageContent;
    private LocalDateTime sentAt;
    private Long chatRoomId;
    private Long senderId;

}
