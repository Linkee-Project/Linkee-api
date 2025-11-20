package com.linkee.linkeeapi.board.report.command.application.service;

import com.linkee.linkeeapi.board.report.command.application.dto.request.CreateReportRequestDto;
import com.linkee.linkeeapi.board.report.command.application.dto.request.UpdateReportActionRequestDto;
import com.linkee.linkeeapi.common.model.CustomUser;

public interface ReportCommandService {

    //create
    void createReport(Long reporterId, CreateReportRequestDto request);

    //update
    void updateReportAction(Long adminId, UpdateReportActionRequestDto request);
}
