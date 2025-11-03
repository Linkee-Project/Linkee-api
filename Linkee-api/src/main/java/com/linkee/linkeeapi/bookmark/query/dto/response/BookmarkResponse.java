package com.linkee.linkeeapi.bookmark.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkResponse {
    private Long bookmarkId;
    private Long userId;
    private String userNickname;
    private Long questionId;
    private String questionTitle;
}
