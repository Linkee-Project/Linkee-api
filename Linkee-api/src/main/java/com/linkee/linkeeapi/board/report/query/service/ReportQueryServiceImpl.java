package com.linkee.linkeeapi.board.report.query.service;

import com.linkee.linkeeapi.board.report.command.domain.aggregate.ReportType;
import com.linkee.linkeeapi.board.report.query.dto.request.ReadReportListRequestDto;
import com.linkee.linkeeapi.board.report.query.dto.response.ReportDetailResponseDto;
import com.linkee.linkeeapi.board.report.query.dto.response.ReportListResponseDto;
import com.linkee.linkeeapi.board.report.query.mapper.ReportMapper;
import com.linkee.linkeeapi.common.enums.Role;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.users.command.application.service.util.UserFinder;
import com.linkee.linkeeapi.users.command.domain.entity.User;
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

    // 신고 목록 조회
    // 관리자: 전체 / 유저: 본인 것만
    // type(C/B/R/U/G), status(Y/N) 필터 + 페이징
    public List<ReportListResponseDto> getReportList(Long userId, ReadReportListRequestDto request) {

        User user = userFinder.getById(userId);
        boolean isAdmin = user.getUserRole() == Role.ADMIN;

        // 1. 전체 목록 조회
        List<ReportListResponseDto> list;
        if (isAdmin) {
            list = reportMapper.findAllReportsWithoutPaging();
        } else {
            list = reportMapper.findReportsByUserWithoutPaging(userId);
        }

        // 2. 타입 필터
        if (request.getType() != null && !request.getType().isEmpty()) {
            try {
                ReportType typeEnum = ReportType.valueOf(request.getType()); // 문자열 -> enum
                list = list.stream()
                        .filter(r -> r.getReportType() == typeEnum)
                        .toList();
            } catch (IllegalArgumentException e) {
                // 잘못된 type 값이 들어온 경우 → 그냥 비어있는 리스트 반환
                list = List.of();
            }
        }

        // 3. 상태 필터
        if (request.getStatus() != null && !request.getStatus().isEmpty()) {
            try {
                Status statusEnum = Status.valueOf(request.getStatus());
                list = list.stream()
                        .filter(r -> r.getReportStatus() == statusEnum)
                        .toList();
            } catch (IllegalArgumentException e) {
                list = List.of();
            }
        }

        // 4. 페이징 처리
        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() != null ? request.getSize() : 10;

        int fromIdx = page * size;
        int toIdx = Math.min(fromIdx + size, list.size());

        if (fromIdx >= list.size()) {
            return List.of();
        }

        return list.subList(fromIdx, toIdx);
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
