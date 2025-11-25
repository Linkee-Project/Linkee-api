package com.linkee.linkeeapi.board.report.query.dto.response;

import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.board.report.command.domain.aggregate.ReportType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportListResponseDto {

    private Long reportId;
    private String reportTitle;
    private ReportType reportType;

    private Long reporterId;
    private Long adminId;

    private Status reportStatus;

    // 선택 옵션 (JOIN 필요)
    private String reporterNickname;
    private String adminNickname;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

