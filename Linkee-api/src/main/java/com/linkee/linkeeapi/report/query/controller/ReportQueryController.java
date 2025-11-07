package com.linkee.linkeeapi.report.query.controller;

import com.linkee.linkeeapi.report.query.dto.request.ReadReportListRequestDto;
import com.linkee.linkeeapi.report.query.dto.response.ReportDetailResponseDto;
import com.linkee.linkeeapi.report.query.dto.response.ReportListResponseDto;
import com.linkee.linkeeapi.report.query.service.ReportQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/report")
@Tag(name = "신고", description = "사용자 신고 및 관리자 처리 API")
public class ReportQueryController {
    private final ReportQueryService reportService;


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
}
