package com.linkee.linkeeapi.board.report.command.application.dto.request;

import com.linkee.linkeeapi.board.report.command.domain.aggregate.ReportType;
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
    @NotNull private Long reportedId; //피신고자
}
