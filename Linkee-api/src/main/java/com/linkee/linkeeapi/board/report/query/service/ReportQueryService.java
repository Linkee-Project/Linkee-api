package com.linkee.linkeeapi.board.report.query.service;

import com.linkee.linkeeapi.board.report.query.dto.request.ReadReportListRequestDto;
import com.linkee.linkeeapi.board.report.query.dto.response.ReportDetailResponseDto;
import com.linkee.linkeeapi.board.report.query.dto.response.ReportListResponseDto;

import java.util.List;

public interface ReportQueryService {


    //Read
    List<ReportListResponseDto> getReportList(Long userId, ReadReportListRequestDto request);

    ReportDetailResponseDto getReportDetail(Long reportId, Long userId);

}
