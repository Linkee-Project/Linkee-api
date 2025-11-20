package com.linkee.linkeeapi.board.report.command.application.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateReportActionRequestDto {
    private Long reportId;
    private String reportAction;
}
