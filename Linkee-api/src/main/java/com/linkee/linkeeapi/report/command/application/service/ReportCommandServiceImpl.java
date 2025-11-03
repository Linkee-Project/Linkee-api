package com.linkee.linkeeapi.report.command.application.service;

import com.linkee.linkeeapi.common.enums.Role;
import com.linkee.linkeeapi.common.enums.Status;

import com.linkee.linkeeapi.report.command.application.dto.request.CreateReportRequestDto;
import com.linkee.linkeeapi.report.command.application.dto.request.UpdateReportActionRequestDto;
import com.linkee.linkeeapi.report.command.domain.aggregate.Report;
import com.linkee.linkeeapi.report.command.infrastructure.repository.ReportRepository;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user.command.application.service.util.UserFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportCommandServiceImpl implements ReportCommandService {

    private final ReportRepository reportRepository;
    private final UserFinder userFinder;

    //신고 등록
    @Override
    public void createReport(CreateReportRequestDto request) {
        // 신고자 및 피신고자 조회
        User reporter = userFinder.getById(request.getReporterId());
        User reported = userFinder.getById(request.getReportedId());

        // Report 엔티티 생성 (Builder 사용)
        Report report = Report.builder()
                .reportUser(reporter)
                .reportedUser(reported)
                .reportTitle(request.getReportTitle())
                .reportContent(request.getReportContent())
                .reportType(request.getReportType())
                .reportStatus(Status.N) // 기본 상태: 미처리
                .build();

        // 저장
        reportRepository.save(report);
    }


    // 신고 처리 입력
    @Override
    public void updateReportAnswer(UpdateReportActionRequestDto request) {
        // 관리자 조회
        User adminUser = userFinder.getById(request.getAdminId());

        // 권한 확인
        if (adminUser.getUserRole() != Role.ADMIN) {
            throw new IllegalStateException("관리자만 신고 처리가 가능합니다.");
        }

        // 신고 조회
        Report report = reportRepository.findById(request.getReportId())
                .orElseThrow(() -> new IllegalArgumentException("해당 신고를 찾을 수 없습니다."));

        // 도메인 내부 메서드로 상태 변경
        // setter사용 지양해야하니까
        report.handleReport(adminUser, request.getReportAction());

        reportRepository.save(report);
    }


}
