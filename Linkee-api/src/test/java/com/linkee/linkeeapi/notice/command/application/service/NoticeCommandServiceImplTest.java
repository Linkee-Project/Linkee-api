/*
package com.linkee.linkeeapi.notice.command.application.service;

import com.linkee.linkeeapi.board.notice.command.application.dto.request.CreateNoticeRequestDto;
import com.linkee.linkeeapi.board.notice.command.application.dto.request.UpdateNoticeRequestDto;
import com.linkee.linkeeapi.board.notice.command.application.service.NoticeCommandServiceImpl;
import com.linkee.linkeeapi.board.notice.command.domain.aggregate.entity.Notice;
import com.linkee.linkeeapi.board.notice.command.infrastructure.repository.NoticeRepository;
import com.linkee.linkeeapi.board.notice.query.mapper.NoticeMapper;
import com.linkee.linkeeapi.common.enums.Role;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.common.security.model.CustomUser;
import com.linkee.linkeeapi.user.command.application.service.util.UserFinder;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NoticeCommandServiceImplTest {

    private NoticeCommandServiceImpl noticeCommandService;
    private UserFinder userFinder;
    private ModelMapper modelMapper;
    private NoticeRepository noticeRepository;
    private NoticeMapper noticeMapper;

    private User adminUser;
    private User normalUser;
    private CustomUser adminCustomUser;
    private CustomUser normalCustomUser;

    @BeforeEach
    void setUp() {
        userFinder = mock(UserFinder.class);
        modelMapper = new ModelMapper();
        noticeRepository = mock(NoticeRepository.class);
        noticeMapper = mock(NoticeMapper.class);

        noticeCommandService = new NoticeCommandServiceImpl(userFinder, modelMapper, noticeRepository, noticeMapper);

        adminUser = User.builder()
                .userId(1L)
                .userRole(Role.ADMIN)
                .build();

        normalUser = User.builder()
                .userId(2L)
                .userRole(Role.USER)
                .build();

        adminCustomUser = new CustomUser(1L, "admin@example.com", "password", "ROLE_ADMIN");
        normalCustomUser = new CustomUser(2L, "user@example.com", "password", "ROLE_USER");
    }

    // ---------------------------------------------------------
    // ✅ 공지사항 등록
    // ---------------------------------------------------------
    @Test
    @DisplayName("Noti_Test_001 공지사항 등록 성공 - 관리자 권한")
    void createNotice_success() {
        // given
        CreateNoticeRequestDto request = CreateNoticeRequestDto.builder()
                .noticeTitle("공지 제목")
                .noticeContent("공지 내용")
                .build();

        when(userFinder.getById(1L)).thenReturn(adminUser);
        when(noticeRepository.save(any(Notice.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when & then
        assertThatCode(() -> noticeCommandService.createNotice(adminCustomUser, request))
                .doesNotThrowAnyException();

        verify(noticeRepository, times(1)).save(any(Notice.class));
    }

    @Test
    @DisplayName("Noti_Test_001 공지사항 등록 실패 - 일반 사용자 접근")
    void createNotice_fail_notAdmin() {
        // given
        CreateNoticeRequestDto request = CreateNoticeRequestDto.builder()
                .noticeTitle("공지 제목")
                .noticeContent("공지 내용")
                .build();

        when(userFinder.getById(2L)).thenReturn(normalUser);

        // when & then
        assertThatThrownBy(() -> noticeCommandService.createNotice(normalCustomUser, request))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.UNAUTHORIZED_ACCESS.getMessage());

        verify(noticeRepository, never()).save(any());
    }

    // ---------------------------------------------------------
    // ✅ 공지사항 수정
    // ---------------------------------------------------------
    @Test
    @DisplayName("Noti_Test_002 공지사항 수정 성공 - 관리자 권한")
    void updateNotice_success() {
        // given
        Notice notice = Notice.builder()
                .noticeId(1L)
                .noticeTitle("old title")
                .noticeContent("old content")
                .isDeleted(Status.N)
                .build();

        UpdateNoticeRequestDto request = new UpdateNoticeRequestDto(1L, 1L, "new title", "new content");

        when(noticeRepository.findById(1L)).thenReturn(Optional.of(notice));
        when(userFinder.getById(1L)).thenReturn(adminUser);

        // when
        noticeCommandService.updateNotice(adminCustomUser, request);

        // then
        assertThat(notice.getNoticeTitle()).isEqualTo("new title");
        assertThat(notice.getNoticeContent()).isEqualTo("new content");
    }

    @Test
    @DisplayName("Noti_Test_002 공지사항 수정 실패 - 관리자 아님")
    void updateNotice_fail_notAdmin() {
        // given
        Notice notice = Notice.builder()
                .noticeId(1L)
                .noticeTitle("old title")
                .noticeContent("old content")
                .isDeleted(Status.N)
                .build();

        UpdateNoticeRequestDto request = new UpdateNoticeRequestDto(1L, 2L, "new title", "new content");

        when(noticeRepository.findById(1L)).thenReturn(Optional.of(notice));
        when(userFinder.getById(2L)).thenReturn(normalUser);

        // when & then
        assertThatThrownBy(() -> noticeCommandService.updateNotice(normalCustomUser, request))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.UNAUTHORIZED_ACCESS.getMessage());
    }

    // ---------------------------------------------------------
    // ✅ 공지사항 삭제
    // ---------------------------------------------------------
    @Test
    @DisplayName("Noti_Test_003 공지사항 삭제 성공 - 관리자 권한")
    void deleteNotice_success() {
        // given
        Notice notice = Notice.builder()
                .noticeId(1L)
                .isDeleted(Status.N)
                .build();

        when(noticeRepository.findById(1L)).thenReturn(Optional.of(notice));
        when(userFinder.getById(1L)).thenReturn(adminUser);

        // when
        noticeCommandService.deleteNotice(adminCustomUser, 1L);

        // then
        assertThat(notice.getIsDeleted()).isEqualTo(Status.Y);
        verify(noticeRepository, times(1)).save(notice);
    }

    @Test
    @DisplayName("Noti_Test_003 공지사항 삭제 실패 - 관리자 아님")
    void deleteNotice_fail_notAdmin() {
        // given
        Notice notice = Notice.builder()
                .noticeId(1L)
                .isDeleted(Status.N)
                .build();

        when(noticeRepository.findById(1L)).thenReturn(Optional.of(notice));
        when(userFinder.getById(2L)).thenReturn(normalUser);

        // when & then
        assertThatThrownBy(() -> noticeCommandService.deleteNotice(normalCustomUser, 1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.UNAUTHORIZED_ACCESS.getMessage());

        verify(noticeRepository, never()).save(any());
    }
}
*/
