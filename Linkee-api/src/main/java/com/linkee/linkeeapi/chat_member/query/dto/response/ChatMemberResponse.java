package com.linkee.linkeeapi.chat_member.query.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMemberResponse {

    private Long chatMemberId;
    private LocalDateTime joinedAt;
    private LocalDateTime leftAt;
    private Long chatRoomId;
    private Long userId;
    private String isRead;

}
