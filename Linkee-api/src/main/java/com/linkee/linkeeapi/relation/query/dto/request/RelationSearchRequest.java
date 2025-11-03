package com.linkee.linkeeapi.relation.query.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelationSearchRequest {
    private Long userId;
    private String relationStatus;
    private Integer page;
    private Integer size;
    private Integer offset;
}
