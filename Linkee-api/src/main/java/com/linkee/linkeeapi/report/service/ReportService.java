package com.linkee.linkeeapi.report.service;

import com.linkee.linkeeapi.report.model.dto.request.CreateReportRequestDto;
import com.linkee.linkeeapi.report.model.dto.request.ReadReportListRequestDto;
import com.linkee.linkeeapi.report.model.dto.request.UpdateReportActionRequestDto;
import com.linkee.linkeeapi.report.model.dto.response.ReportDetailResponseDto;
import com.linkee.linkeeapi.report.model.dto.response.ReportListResponseDto;

import java.util.List;

public interface ReportService {

    //create
    void createReport(CreateReportRequestDto request);

    //Read
    List<ReportListResponseDto> getReportList(ReadReportListRequestDto request);

    ReportDetailResponseDto getReportDetail(Long reportId, Long userId);

    //update
    void updateReportAnswer(UpdateReportActionRequestDto request);
}
