package com.linkee.linkeeapi.users.command.application.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelationCreateRequest {
    private Long requesterId;
    private Long receiverId;
}
