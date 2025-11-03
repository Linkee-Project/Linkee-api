package com.linkee.linkeeapi.report.query.dto.response;

import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.report.command.domain.aggregate.ReportType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//목록 조회 응답
public class ReportListResponseDto {

    private String reportTitle;
    private ReportType reportType;
    private Long reporterId;
    private Status reportStatus;

}
