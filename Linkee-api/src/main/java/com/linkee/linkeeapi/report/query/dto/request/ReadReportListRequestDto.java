package com.linkee.linkeeapi.report.query.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReadReportListRequestDto {

    private Long userId;

    private Integer page;
    private Integer size;
    //private String sort;      // 정렬 기준 (예: "created_at DESC", "report_status ASC")
}
