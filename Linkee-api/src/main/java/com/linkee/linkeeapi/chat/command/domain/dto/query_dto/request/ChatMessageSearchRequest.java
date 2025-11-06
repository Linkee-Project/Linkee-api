package com.linkee.linkeeapi.chat.command.domain.dto.query_dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageSearchRequest {

    private String keyword;
    private Integer page;
    private Integer size;
    private Integer offset;
    private Long chatRoomId;
    private Long senderId;


}

