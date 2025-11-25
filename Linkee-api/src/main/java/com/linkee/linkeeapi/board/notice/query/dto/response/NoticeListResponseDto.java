package com.linkee.linkeeapi.board.notice.query.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//목록 조회 응답
public class NoticeListResponseDto {

    private Long noticeId;
    private String noticeTitle;
    private Long noticeViews;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String adminName;
    private String isActive;


}
