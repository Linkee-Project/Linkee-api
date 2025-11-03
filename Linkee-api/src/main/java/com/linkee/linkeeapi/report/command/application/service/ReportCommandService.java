package com.linkee.linkeeapi.report.command.application.service;

import com.linkee.linkeeapi.report.command.application.dto.request.CreateReportRequestDto;
import com.linkee.linkeeapi.report.command.application.dto.request.UpdateReportActionRequestDto;

public interface ReportCommandService {

    //create
    void createReport(CreateReportRequestDto request);

    //update
    void updateReportAnswer(UpdateReportActionRequestDto request);
}
