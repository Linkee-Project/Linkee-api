package com.linkee.linkeeapi.report.model.dto.response;

import com.linkee.linkeeapi.common.enums.ReportType;
import com.linkee.linkeeapi.common.enums.Status;
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
