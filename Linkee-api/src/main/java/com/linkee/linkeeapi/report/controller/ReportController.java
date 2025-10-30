package com.linkee.linkeeapi.report.controller;

import com.linkee.linkeeapi.report.model.dto.request.CreateReportRequestDto;
import com.linkee.linkeeapi.report.model.dto.request.ReadReportListRequestDto;
import com.linkee.linkeeapi.report.model.dto.request.UpdateReportActionRequestDto;
import com.linkee.linkeeapi.report.model.dto.response.ReportDetailResponseDto;
import com.linkee.linkeeapi.report.model.dto.response.ReportListResponseDto;
import com.linkee.linkeeapi.report.service.ReportService;
import com.linkee.linkeeapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/report")
public class ReportController {
    private final ReportService reportService;
    private final UserRepository userRepository;

    //create
    @PostMapping
    public ResponseEntity<String> createReport(@RequestBody CreateReportRequestDto request) {
        reportService.createReport(request);
        return ResponseEntity.ok("신고 생성 완료");
    }

    //목록 조회
    //관리자 전체조회
    //일반 유저 자신 신고만 조회
    @GetMapping
    public ResponseEntity<List<ReportListResponseDto>> getReportList(
            @RequestParam Long userId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        ReadReportListRequestDto request = ReadReportListRequestDto.builder()
                .userId(userId)
                .page(page)
                .size(size)
                .build();

        List<ReportListResponseDto> reports = reportService.getReportList(request);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{reportId}/{userId}")
    public ResponseEntity<ReportDetailResponseDto> getReportDetail(
            @PathVariable Long reportId,
            @PathVariable Long userId) {

        ReportDetailResponseDto reportDetail = reportService.getReportDetail(reportId, userId);
        return ResponseEntity.ok(reportDetail);
    }

    //신고 처리
    @PatchMapping("/action")
    public ResponseEntity<String> updateReportAnswer(
            @RequestBody UpdateReportActionRequestDto request){

        reportService.updateReportAnswer(request);
        return ResponseEntity.ok("신고 처리 완료");
    }



}
