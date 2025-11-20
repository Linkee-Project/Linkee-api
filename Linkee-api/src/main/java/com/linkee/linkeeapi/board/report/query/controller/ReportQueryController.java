package com.linkee.linkeeapi.board.report.query.controller;

import com.linkee.linkeeapi.board.report.query.dto.request.ReadReportListRequestDto;
import com.linkee.linkeeapi.board.report.query.dto.response.ReportDetailResponseDto;
import com.linkee.linkeeapi.board.report.query.dto.response.ReportListResponseDto;
import com.linkee.linkeeapi.board.report.query.service.ReportQueryService;
import com.linkee.linkeeapi.common.model.CustomUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/board/reports")
@Tag(name = "신고", description = "사용자 신고 및 관리자 처리 API")
public class ReportQueryController {
    private final ReportQueryService reportService;


    //목록 조회
    //관리자 전체조회
    //일반 유저 자신 신고만 조회
    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<List<ReportListResponseDto>> getReportList(
            @AuthenticationPrincipal CustomUser customUser,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        Long userId = customUser.getUserId();
        ReadReportListRequestDto request = ReadReportListRequestDto.builder()
                .page(page)
                .size(size)
                .build();

        List<ReportListResponseDto> reports = reportService.getReportList(userId, request);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{reportId}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<ReportDetailResponseDto> getReportDetail(
            @AuthenticationPrincipal CustomUser customUser,
            @PathVariable Long reportId
    ) {

        ReportDetailResponseDto reportDetail = reportService.getReportDetail(reportId, customUser.getUserId());
        return ResponseEntity.ok(reportDetail);
    }
}
