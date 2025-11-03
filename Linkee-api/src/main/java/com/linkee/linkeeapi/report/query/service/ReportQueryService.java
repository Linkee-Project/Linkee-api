package com.linkee.linkeeapi.report.query.service;

import com.linkee.linkeeapi.report.command.application.dto.request.CreateReportRequestDto;
import com.linkee.linkeeapi.report.command.application.dto.request.UpdateReportActionRequestDto;
import com.linkee.linkeeapi.report.query.dto.request.ReadReportListRequestDto;
import com.linkee.linkeeapi.report.query.dto.response.ReportDetailResponseDto;
import com.linkee.linkeeapi.report.query.dto.response.ReportListResponseDto;

import java.util.List;

public interface ReportQueryService {


    //Read
    List<ReportListResponseDto> getReportList(ReadReportListRequestDto request);

    ReportDetailResponseDto getReportDetail(Long reportId, Long userId);

}
