package com.linkee.linkeeapi.bookmark.query.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkSearchRequest {
    private Long userId;
    private Integer page;
    private Integer size;
    private Integer offset;
    private Long questionId;
}