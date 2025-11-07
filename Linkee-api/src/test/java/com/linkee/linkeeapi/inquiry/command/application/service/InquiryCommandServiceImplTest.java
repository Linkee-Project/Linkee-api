package com.linkee.linkeeapi.inquiry.command.application.service;

import com.linkee.linkeeapi.common.enums.Role;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.event.InquiryAnsweredEvent;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.inquiry.command.application.dto.request.CreateInquiryRequestDto;
import com.linkee.linkeeapi.inquiry.command.application.dto.request.UpdateInquiryAnswerRequestDto;
import com.linkee.linkeeapi.inquiry.command.domain.aggregate.Inquiry;
import com.linkee.linkeeapi.inquiry.command.infrastructure.repository.JpaInquiryRepository;
import com.linkee.linkeeapi.user.command.application.service.util.UserFinder;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InquiryCommandServiceImplTest {

    @Mock
    private JpaInquiryRepository inquiryRepository;
    @Mock
    private UserFinder userFinder;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private InquiryCommandServiceImpl inquiryCommandService;

    private User mockUser;
    private User adminUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUser = User.builder()
                .userId(1L)
                .userRole(Role.USER)
                .build();

        adminUser = User.builder()
                .userId(2L)
                .userRole(Role.ADMIN)
                .build();
    }

    // ===========================
    // CREATE INQUIRY TEST
    // ===========================

    @Test
    @DisplayName("Inq_Test_001 문의글 등록 성공")
    void createInquiry_Success() {
        // given
        CreateInquiryRequestDto request = CreateInquiryRequestDto.builder()
                .inquiryTitle("테스트 제목")
                .inquiryContent("테스트 내용")
                .userId(1L)
                .build();

        when(userFinder.getById(1L)).thenReturn(mockUser);

        // when
        inquiryCommandService.createInquiry(request);

        // then
        verify(inquiryRepository, times(1)).save(any(Inquiry.class));
    }

    @Test
    @DisplayName("Inq_Test_002 문의글 등록 실패 - 사용자 ID 누락")
    void createInquiry_Fail_NoUserId() {
        // given
        CreateInquiryRequestDto request = CreateInquiryRequestDto.builder()
                .inquiryTitle("테스트 제목")
                .inquiryContent("내용")
                .userId(null)
                .build();

        // when / then
        assertThatThrownBy(() -> inquiryCommandService.createInquiry(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.INVALID_USER_ID.getMessage());
    }

    @Test
    @DisplayName("Inq_Test_003 문의글 등록 실패 - 제목 누락")
    void createInquiry_Fail_NoTitle() {
        CreateInquiryRequestDto request = CreateInquiryRequestDto.builder()
                .inquiryTitle("")
                .inquiryContent("내용")
                .userId(1L)
                .build();

        assertThatThrownBy(() -> inquiryCommandService.createInquiry(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("문의 제목은 필수 입력값입니다.");
    }

    // ===========================
    // UPDATE ANSWER TEST
    // ===========================

    @Test
    @DisplayName("Inq_Test_004 문의 답변 등록 성공")
    void updateInquiryAnswer_Success() {
        // given
        UpdateInquiryAnswerRequestDto request = new UpdateInquiryAnswerRequestDto(1L, 2L, "답변 내용");

        Inquiry inquiry = Inquiry.builder()
                .inquiryId(1L)
                .inquiryTitle("문의 제목")
                .inquiryContent("문의 내용")
                .answerStatus(Status.N)
                .user(mockUser)
                .build();

        when(userFinder.getById(2L)).thenReturn(adminUser);
        when(inquiryRepository.findById(1L)).thenReturn(Optional.of(inquiry));

        // when
        inquiryCommandService.updateInquiryAnswer(request);

        // then
        verify(inquiryRepository, times(1)).save(any(Inquiry.class));
        verify(eventPublisher, times(1)).publishEvent(any(InquiryAnsweredEvent.class));
    }

    @Test
    @DisplayName(" Inq_Test_006 문의 답변 실패 - 관리자 권한 아님")
    void updateInquiryAnswer_Fail_NotAdmin() {
        // given
        UpdateInquiryAnswerRequestDto request = new UpdateInquiryAnswerRequestDto(1L, 1L, "답변 내용");
        when(userFinder.getById(1L)).thenReturn(mockUser); // USER 권한임

        // when / then
        assertThatThrownBy(() -> inquiryCommandService.updateInquiryAnswer(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.UNAUTHORIZED_ACCESS.getMessage());
    }

    @Test
    @DisplayName("Inq_Test_005 문의 답변 실패 - 이미 답변 완료된 문의")
    void updateInquiryAnswer_Fail_AlreadyAnswered() {
        // given
        UpdateInquiryAnswerRequestDto request = new UpdateInquiryAnswerRequestDto(1L, 2L, "답변 내용");

        Inquiry inquiry = Inquiry.builder()
                .inquiryId(1L)
                .answerStatus(Status.Y)
                .build();

        when(userFinder.getById(2L)).thenReturn(adminUser);
        when(inquiryRepository.findById(1L)).thenReturn(Optional.of(inquiry));

        // when / then
        assertThatThrownBy(() -> inquiryCommandService.updateInquiryAnswer(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.ALREADY_ANSWERED.getMessage());
    }

    @Test
    @DisplayName("Inq_Test_007 문의 답변 실패 - 존재하지 않는 문의 ID")
    void updateInquiryAnswer_Fail_NotFound() {
        // given
        UpdateInquiryAnswerRequestDto request = new UpdateInquiryAnswerRequestDto(99L, 2L, "답변 내용");
        when(userFinder.getById(2L)).thenReturn(adminUser);
        when(inquiryRepository.findById(99L)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> inquiryCommandService.updateInquiryAnswer(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.INQUIRY_NOT_FOUND.getMessage());
    }
}
