package com.linkee.linkeeapi.board.report.command.application.service;

import com.linkee.linkeeapi.board.report.command.application.dto.request.CreateReportRequestDto;
import com.linkee.linkeeapi.board.report.command.application.dto.request.UpdateReportActionRequestDto;

public interface ReportCommandService {

    //create
    void createReport(CreateReportRequestDto request);

    //update
    void updateReportAnswer(UpdateReportActionRequestDto request);
}
