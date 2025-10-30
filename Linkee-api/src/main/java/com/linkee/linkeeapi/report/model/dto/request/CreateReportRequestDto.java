package com.linkee.linkeeapi.report.model.dto.request;

import com.linkee.linkeeapi.common.enums.ReportType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateReportRequestDto {
    @NotBlank private String reportTitle;
    @NotBlank private String reportContent;
    @NotNull private ReportType reportType;
    @NotNull private Long reporterId; //신고자
    @NotNull private Long reportedId; //피신고자
}
