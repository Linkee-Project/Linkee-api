package com.linkee.linkeeapi.notice.query.service;

import static org.junit.jupiter.api.Assertions.*;


import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.notice.query.dto.response.NoticeDetailResponseDto;
import com.linkee.linkeeapi.notice.query.dto.response.NoticeListResponseDto;
import com.linkee.linkeeapi.notice.query.mapper.NoticeMapper;
import com.linkee.linkeeapi.user.command.application.service.util.UserFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NoticeQueryServiceImplTest {

    @Mock
    private UserFinder userFinder;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private NoticeMapper noticeMapper;

    @InjectMocks
    private NoticeQueryServiceImpl noticeQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Noti_Test_004 공지사항 목록 조회 - 기본 페이지 사이즈")
    void testGetNoticeList_DefaultSize() {
        NoticeListResponseDto notice1 = NoticeListResponseDto.builder()
                .noticeId(1L)
                .noticeTitle("공지1")
                .noticeViews(10L)
                .createdAt(LocalDateTime.now())
                .build();
        NoticeListResponseDto notice2 = NoticeListResponseDto.builder()
                .noticeId(2L)
                .noticeTitle("공지2")
                .noticeViews(20L)
                .createdAt(LocalDateTime.now())
                .build();

        List<NoticeListResponseDto> noticeList = Arrays.asList(notice1, notice2);

        when(noticeMapper.findAll(0, 10)).thenReturn(noticeList);
        when(noticeMapper.countAll()).thenReturn(2);

        PageResponse<NoticeListResponseDto> response = noticeQueryService.getNoticeList(0, null);

        assertEquals(2, response.getContent().size());
        assertEquals(0, response.getCurrentPage());
        assertEquals(2, response.getTotalElements());
    }

    @Test
    @DisplayName("Noti_Test_005 공지사항 상세 조회 - 정상 조회")
    void testGetNoticeDetail_Success() {
        Long noticeId = 1L;

        NoticeDetailResponseDto detail = NoticeDetailResponseDto.builder()
                .noticeTitle("공지1")
                .noticeContent("내용1")
                .noticeViews(10L)
                .createdAt(LocalDateTime.now())
                .adminName("관리자")
                .build();

        when(noticeMapper.increaseViewCount(noticeId)).thenReturn(1);
        when(noticeMapper.findById(noticeId)).thenReturn(detail);

        NoticeDetailResponseDto response = noticeQueryService.getNoticeDetail(noticeId);

        assertNotNull(response);
        assertEquals("공지1", response.getNoticeTitle());
        assertEquals("내용1", response.getNoticeContent());
        assertEquals("관리자", response.getAdminName());
    }

    @Test
    @DisplayName("Noti_Test_005공지사항 상세 조회 - ID가 null일 경우 예외 발생")
    void testGetNoticeDetail_InvalidId() {
        BusinessException exception = assertThrows(BusinessException.class,
                () -> noticeQueryService.getNoticeDetail(null));

        assertEquals(ErrorCode.INVALID_NOTICE_ID, exception.getErrorCode());
    }

    @Test
    @DisplayName("Noti_Test_005 공지사항 상세 조회 - 존재하지 않는 공지사항 예외 발생")
    void testGetNoticeDetail_NotFound() {
        Long noticeId = 1L;

        when(noticeMapper.increaseViewCount(noticeId)).thenReturn(0);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> noticeQueryService.getNoticeDetail(noticeId));

        assertEquals(ErrorCode.NOTICE_NOT_FOUND, exception.getErrorCode());
    }

}
