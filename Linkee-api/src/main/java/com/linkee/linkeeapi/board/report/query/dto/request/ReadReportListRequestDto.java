package com.linkee.linkeeapi.board.report.query.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReadReportListRequestDto {

    private Integer page;
    private Integer size;
    private String type;
    private String status;
}
