package com.linkee.linkeeapi.board.report.command.application.controller;

import com.linkee.linkeeapi.board.report.command.application.dto.request.CreateReportRequestDto;
import com.linkee.linkeeapi.board.report.command.application.dto.request.UpdateReportActionRequestDto;
import com.linkee.linkeeapi.board.report.command.application.service.ReportCommandService;
import com.linkee.linkeeapi.common.model.CustomUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@Tag(name = "신고", description = "사용자 신고 및 관리자 처리 API")
public class ReportCommandController {
    private final ReportCommandService reportService;
    //create
    @PostMapping("/board/reports/new")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<String> createReport(
            @AuthenticationPrincipal CustomUser customUser,
            @RequestBody CreateReportRequestDto request) {

        reportService.createReport(customUser.getUserId(), request);
        return ResponseEntity.ok("신고 생성 완료");
    }

    //신고 처리 //관리자만
    @PatchMapping("/admin/board/reports/action")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> updateReportAnswer(
            @AuthenticationPrincipal CustomUser customUser,
            @RequestBody UpdateReportActionRequestDto request){

        reportService.updateReportAction(customUser.getUserId(), request);
        return ResponseEntity.ok("신고 처리 완료");
    }



}
