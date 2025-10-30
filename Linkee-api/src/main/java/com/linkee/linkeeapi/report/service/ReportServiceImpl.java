package com.linkee.linkeeapi.report.service;

import com.linkee.linkeeapi.common.enums.Role;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.report.mapper.ReportMapper;
import com.linkee.linkeeapi.report.model.dto.request.CreateReportRequestDto;
import com.linkee.linkeeapi.report.model.dto.request.ReadReportListRequestDto;
import com.linkee.linkeeapi.report.model.dto.request.UpdateReportActionRequestDto;
import com.linkee.linkeeapi.report.model.dto.response.ReportDetailResponseDto;
import com.linkee.linkeeapi.report.model.dto.response.ReportListResponseDto;
import com.linkee.linkeeapi.report.model.entity.Report;
import com.linkee.linkeeapi.report.repository.ReportRepository;
import com.linkee.linkeeapi.user.model.entity.User;
import com.linkee.linkeeapi.user.service.util.UserFinder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final UserFinder userFinder;
    private final ModelMapper modelMapper;
    private final ReportMapper reportMapper;

    //신고 등록
    @Override
    public void createReport(CreateReportRequestDto request){
        Report report = modelMapper.map(request, Report.class);
        report.setReportUser(userFinder.getById(request.getReporterId()));
        report.setReportedUser(userFinder.getById(request.getReportedId()));
        reportRepository.save(report);

    }

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
            throw new IllegalStateException("조회 권한이 없거나 존재하지 않는 신고입니다.");
        }

        return reportDetail;
    }

    // 신고 처리 입력
    @Override
    @Transactional(readOnly = false)
    public void updateReportAnswer(UpdateReportActionRequestDto request){
        //관리자 조회
        User adminUser = userFinder.getById(request.getAdminId());

        //관리자 권한 체크
        if (adminUser.getUserRole() != Role.ADMIN) {
            throw new IllegalStateException("관리자만 답변 등록 가능");
        }

        //신고 조회
        Report report = reportRepository.findById(request.getReportId())
                .orElseThrow(() -> new IllegalArgumentException("해당 신고를 찾을 수 없습니다."));

        //신고 처리 등록
        report.setAdmin(adminUser);
        report.setReportAction(request.getReportAction());
        report.setReportStatus(Status.Y);

        reportRepository.save(report);
    }


}
