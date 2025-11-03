package com.linkee.linkeeapi.relation.query.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelationResponse {
    private Long relationId;
    private Long requesterId;
    private String requesterNickname;
    private Long receiverId;
    private String receiverNickname;
    private String relationStatus;
    private String acceptedAt;
}
