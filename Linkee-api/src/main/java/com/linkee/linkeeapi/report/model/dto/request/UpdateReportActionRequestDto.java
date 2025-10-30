package com.linkee.linkeeapi.report.model.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateReportActionRequestDto {
    private Long reportId;
    private Long adminId;
    private String reportAction;
}
