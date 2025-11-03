package com.linkee.linkeeapi.report.command.application.controller;

import com.linkee.linkeeapi.report.command.application.dto.request.CreateReportRequestDto;
import com.linkee.linkeeapi.report.command.application.dto.request.UpdateReportActionRequestDto;
import com.linkee.linkeeapi.report.command.application.service.ReportCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/report")
public class ReportCommandController {
    private final ReportCommandService reportService;
    //create
    @PostMapping
    public ResponseEntity<String> createReport(@RequestBody CreateReportRequestDto request) {
        reportService.createReport(request);
        return ResponseEntity.ok("신고 생성 완료");
    }

    //신고 처리
    @PatchMapping("/action")
    public ResponseEntity<String> updateReportAnswer(
            @RequestBody UpdateReportActionRequestDto request){

        reportService.updateReportAnswer(request);
        return ResponseEntity.ok("신고 처리 완료");
    }



}
