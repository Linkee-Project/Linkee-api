package com.linkee.linkeeapi.report.query.service;

import com.linkee.linkeeapi.common.enums.Role;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.report.query.dto.request.ReadReportListRequestDto;
import com.linkee.linkeeapi.report.query.dto.response.ReportDetailResponseDto;
import com.linkee.linkeeapi.report.query.dto.response.ReportListResponseDto;
import com.linkee.linkeeapi.report.query.mapper.ReportMapper;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user.command.application.service.util.UserFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportQueryServiceImplTest {

    @Mock
    private UserFinder userFinder;

    @Mock
    private ReportMapper reportMapper;

    @InjectMocks
    private ReportQueryServiceImpl reportQueryService;

    private User adminUser;
    private User normalUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        adminUser = User.builder()
                .userId(1L)
                .userRole(Role.ADMIN)
                .build();

        normalUser = User.builder()
                .userId(2L)
                .userRole(Role.USER)
                .build();
    }

    @Test
    @DisplayName("Rep_Test_005 관리자 신고 목록 조회 - 전체 조회")
    void testGetReportList_Admin() {
        ReadReportListRequestDto request = ReadReportListRequestDto.builder()
                .userId(adminUser.getUserId())
                .page(0)
                .size(10)
                .build();

        ReportListResponseDto report1 = ReportListResponseDto.builder()
                .reportTitle("부적절 게시글")
                .reportType(null)
                .reporterId(2L)
                .reportStatus(Status.N)
                .build();

        ReportListResponseDto report2 = ReportListResponseDto.builder()
                .reportTitle("욕설")
                .reportType(null)
                .reporterId(3L)
                .reportStatus(Status.Y)
                .build();

        List<ReportListResponseDto> reportList = Arrays.asList(report1, report2);

        when(userFinder.getById(adminUser.getUserId())).thenReturn(adminUser);
        when(reportMapper.findAllReports(0, 10)).thenReturn(reportList);

        List<ReportListResponseDto> response = reportQueryService.getReportList(request);

        assertEquals(2, response.size());
        verify(reportMapper, times(1)).findAllReports(0, 10);
        verify(reportMapper, never()).findReportsByUser(anyLong(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("Rep_Test_003 일반 사용자 신고 목록 조회 - 본인 신고만")
    void testGetReportList_NormalUser() {
        ReadReportListRequestDto request = ReadReportListRequestDto.builder()
                .userId(normalUser.getUserId())
                .page(0)
                .size(10)
                .build();

        ReportListResponseDto report1 = ReportListResponseDto.builder()
                .reportTitle("부적절 게시글")
                .reportType(null)
                .reporterId(normalUser.getUserId())
                .reportStatus(Status.N)
                .build();

        List<ReportListResponseDto> reportList = Arrays.asList(report1);

        when(userFinder.getById(normalUser.getUserId())).thenReturn(normalUser);
        when(reportMapper.findReportsByUser(normalUser.getUserId(), 0, 10)).thenReturn(reportList);

        List<ReportListResponseDto> response = reportQueryService.getReportList(request);

        assertEquals(1, response.size());
        verify(reportMapper, times(1)).findReportsByUser(normalUser.getUserId(), 0, 10);
        verify(reportMapper, never()).findAllReports(anyInt(), anyInt());
    }

    @Test
    @DisplayName("Rep_Test_006 신고 상세 조회 - 관리자 접근")
    void testGetReportDetail_Admin() {
        Long reportId = 1L;
        ReportDetailResponseDto detail = ReportDetailResponseDto.builder()
                .reportTitle("부적절 게시글")
                .reportContent("내용")
                .reporterId(2L)
                .reportedId(3L)
                .reportStatus(Status.N)
                .reportAction(null)
                .build();

        when(userFinder.getById(adminUser.getUserId())).thenReturn(adminUser);
        when(reportMapper.findReportById(reportId)).thenReturn(detail);

        ReportDetailResponseDto response = reportQueryService.getReportDetail(reportId, adminUser.getUserId());

        assertNotNull(response);
        assertEquals("부적절 게시글", response.getReportTitle());
    }

    @Test
    @DisplayName("Rep_Test_006 신고 상세 조회 - 일반 사용자 접근, 본인 신고")
    void testGetReportDetail_NormalUser_Success() {
        Long reportId = 1L;
        ReportDetailResponseDto detail = ReportDetailResponseDto.builder()
                .reportTitle("부적절 게시글")
                .reportContent("내용")
                .reporterId(normalUser.getUserId())
                .reportedId(3L)
                .reportStatus(Status.N)
                .reportAction(null)
                .build();

        when(userFinder.getById(normalUser.getUserId())).thenReturn(normalUser);
        when(reportMapper.findReportByIdAndReporter(reportId, normalUser.getUserId())).thenReturn(detail);

        ReportDetailResponseDto response = reportQueryService.getReportDetail(reportId, normalUser.getUserId());

        assertNotNull(response);
        assertEquals(normalUser.getUserId(), response.getReporterId());
    }

    @Test
    @DisplayName("Rep_Test_004 신고 상세 조회 - 일반 사용자 접근, 다른 사람 신고 접근 시 예외")
    void testGetReportDetail_NormalUser_NoAccess() {
        Long reportId = 1L;

        when(userFinder.getById(normalUser.getUserId())).thenReturn(normalUser);
        when(reportMapper.findReportByIdAndReporter(reportId, normalUser.getUserId())).thenReturn(null);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> reportQueryService.getReportDetail(reportId, normalUser.getUserId()));

        assertEquals(ErrorCode.REPORT_NO_ACCESS, exception.getErrorCode());
    }

}
