package com.linkee.linkeeapi.report.command.application.service;
import com.linkee.linkeeapi.common.enums.Role;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.report.command.application.dto.request.CreateReportRequestDto;
import com.linkee.linkeeapi.report.command.application.dto.request.UpdateReportActionRequestDto;
import com.linkee.linkeeapi.report.command.domain.aggregate.Report;
import com.linkee.linkeeapi.report.command.domain.aggregate.ReportType;
import com.linkee.linkeeapi.report.command.infrastructure.repository.ReportRepository;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user.command.application.service.util.UserFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportCommandServiceImplTest {

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private UserFinder userFinder;

    @InjectMocks
    private ReportCommandServiceImpl reportCommandService;

    private User reporter;
    private User reported;
    private User adminUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        reporter = User.builder()
                .userId(1L)
                .userRole(Role.USER)
                .build();

        reported = User.builder()
                .userId(2L)
                .userRole(Role.USER)
                .build();

        adminUser = User.builder()
                .userId(3L)
                .userRole(Role.ADMIN)
                .build();
    }

    @Test
    @DisplayName("Rep_Test_001 신고 등록 - 정상 처리")
    void testCreateReport_Success() {
        CreateReportRequestDto request = CreateReportRequestDto.builder()
                .reporterId(reporter.getUserId())
                .reportedId(reported.getUserId())
                .reportTitle("부적절한 게시글")
                .reportContent("내용")
                .reportType(ReportType.B)
                .build();

        when(userFinder.getById(reporter.getUserId())).thenReturn(reporter);
        when(userFinder.getById(reported.getUserId())).thenReturn(reported);

        reportCommandService.createReport(request);

        verify(reportRepository, times(1)).save(any(Report.class));
    }

    @Test
    @DisplayName("Rep_Test_002 신고 처리 - 정상 처리")
    void testUpdateReportAnswer_Success() {
        UpdateReportActionRequestDto request = UpdateReportActionRequestDto.builder()
                .reportId(1L)
                .adminId(adminUser.getUserId())
                .reportAction("처리 완료")
                .build();

        Report report = Report.builder()
                .reportId(1L)
                .reportStatus(Status.N)
                .build();

        when(userFinder.getById(adminUser.getUserId())).thenReturn(adminUser);
        when(reportRepository.findById(request.getReportId())).thenReturn(Optional.of(report));

        reportCommandService.updateReportAnswer(request);

        assertEquals(Status.Y, report.getReportStatus());
        assertEquals("처리 완료", report.getReportAction());
        assertEquals(adminUser, report.getAdmin());
        verify(reportRepository, times(1)).save(report);
    }

    @Test
    @DisplayName("Rep_Test_002 신고 처리 - 관리자 권한 없는 경우 예외")
    void testUpdateReportAnswer_Unauthorized() {
        User normalUser = User.builder()
                .userId(4L)
                .userRole(Role.USER)
                .build();

        UpdateReportActionRequestDto request = UpdateReportActionRequestDto.builder()
                .reportId(1L)
                .adminId(normalUser.getUserId())
                .reportAction("처리 완료")
                .build();

        when(userFinder.getById(normalUser.getUserId())).thenReturn(normalUser);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> reportCommandService.updateReportAnswer(request));

        assertEquals(ErrorCode.UNAUTHORIZED_ACCESS, exception.getErrorCode());
        verify(reportRepository, never()).save(any());
    }

    @Test
    @DisplayName("Rep_Test_002 신고 처리 - 존재하지 않는 신고 예외")
    void testUpdateReportAnswer_ReportNotFound() {
        UpdateReportActionRequestDto request = UpdateReportActionRequestDto.builder()
                .reportId(999L)
                .adminId(adminUser.getUserId())
                .reportAction("처리 완료")
                .build();

        when(userFinder.getById(adminUser.getUserId())).thenReturn(adminUser);
        when(reportRepository.findById(request.getReportId())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> reportCommandService.updateReportAnswer(request));

        assertEquals(ErrorCode.REPORT_NOT_FOUND, exception.getErrorCode());
        verify(reportRepository, never()).save(any());
    }


}
