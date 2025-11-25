package com.linkee.linkeeapi.board.report.query.dto.response;

import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.board.report.command.domain.aggregate.ReportType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//상세 조회 응답
public class ReportDetailResponseDto {

    private String reportTitle;
    private String reportContent;
    private ReportType reportType;

    private Long reporterId;
    private Long reportedId;

    private String reporterNickname;
    private String reportedNickname;
    private String adminNickname;

    private String reportAction;
    private Status reportStatus;

}
