package com.linkee.linkeeapi.notice.query.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//목록 조회 응답
public class NoticeListResponseDto {

    private String noticeTitle;
    //private String noticeContent;
    private Long noticeViews;
    private LocalDateTime createdAt;

}
