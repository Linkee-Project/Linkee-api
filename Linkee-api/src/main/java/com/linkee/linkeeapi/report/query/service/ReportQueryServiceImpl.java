package com.linkee.linkeeapi.report.query.service;

import com.linkee.linkeeapi.common.enums.Role;

import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.report.query.dto.request.ReadReportListRequestDto;
import com.linkee.linkeeapi.report.query.dto.response.ReportDetailResponseDto;
import com.linkee.linkeeapi.report.query.dto.response.ReportListResponseDto;
import com.linkee.linkeeapi.report.query.mapper.ReportMapper;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user.command.application.service.util.UserFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportQueryServiceImpl implements ReportQueryService {

    private final UserFinder userFinder;
    private final ReportMapper reportMapper;


    //신고 목록 조회
    //페이징, 관리자(전체조회)/일반유저(자기 신고만 조회), (시간순, reportStatus, reportType)정렬방식도 추가
    public List<ReportListResponseDto> getReportList (ReadReportListRequestDto request) {

        User user = userFinder.getById(request.getUserId());  // DB에서 user 조회
        boolean isAdmin = false;
        if(user.getUserRole() == Role.ADMIN){
             isAdmin = true;
        }

        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() != null ? request.getSize() : 10;
        int offset = page * size;

        if (isAdmin) {
            return reportMapper.findAllReports(offset, size);
        } else {
            return reportMapper.findReportsByUser(request.getUserId(), offset, size);
        }

    }

    // 신고 상세 조회
    @Override
    public ReportDetailResponseDto getReportDetail(Long reportId, Long userId) {

        User user = userFinder.getById(userId);
        boolean isAdmin = user.getUserRole() == Role.ADMIN;

        ReportDetailResponseDto reportDetail;

        if (isAdmin) {
            reportDetail = reportMapper.findReportById(reportId);
        } else {
            reportDetail = reportMapper.findReportByIdAndReporter(reportId, userId);
        }

        if (reportDetail == null) {
            throw new BusinessException(ErrorCode.REPORT_NO_ACCESS);
        }


        return reportDetail;
    }



}
