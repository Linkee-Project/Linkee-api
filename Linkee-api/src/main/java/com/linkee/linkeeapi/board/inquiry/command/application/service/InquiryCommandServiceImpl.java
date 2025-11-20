package com.linkee.linkeeapi.board.inquiry.command.application.service;

import com.linkee.linkeeapi.board.inquiry.command.application.dto.request.CreateInquiryRequestDto;
import com.linkee.linkeeapi.board.inquiry.command.application.dto.request.UpdateInquiryAnswerRequestDto;
import com.linkee.linkeeapi.board.inquiry.command.domain.aggregate.Inquiry;
import com.linkee.linkeeapi.board.inquiry.command.infrastructure.repository.JpaInquiryRepository;
import com.linkee.linkeeapi.common.enums.Role;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.event.InquiryAnsweredEvent;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.common.model.CustomUser;
import com.linkee.linkeeapi.users.command.application.service.util.UserFinder;
import com.linkee.linkeeapi.users.command.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryCommandServiceImpl implements InquiryCommandService {

    private final JpaInquiryRepository inquiryRepository;
    private final UserFinder userFinder;
    private final ApplicationEventPublisher eventPublisher;

    //create - builer ver.
    @Override
    public void createInquiry(Long userId,CreateInquiryRequestDto request) {
        User user = userFinder.getById(userId);
        if (request.getInquiryTitle() == null || request.getInquiryTitle().isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "문의 제목은 필수 입력값입니다.");
        }
        if (request.getInquiryContent() == null || request.getInquiryContent().isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "문의 내용을 입력해주세요.");
        }

        //user오류처리는 userFinder가 해줌

        Inquiry inquiry = Inquiry.builder()
                .inquiryTitle(request.getInquiryTitle())
                .inquiryContent(request.getInquiryContent())
                .user(user)
                .admin(null)
                .answerStatus(Status.N)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();


        inquiryRepository.save(inquiry);

    }

    //Update -답변등록
    @Override
    @Transactional
    public void updateInquiryAnswer(CustomUser adminUser, Long inquiryId, UpdateInquiryAnswerRequestDto request) {

        User admin = validateAdmin(adminUser.getUserId());
        Inquiry inquiry = validateInquiryAvailable(inquiryId);

        inquiry.setAnswerContent(request.getAnswerContent());
        inquiry.setAnswerStatus(Status.Y);
        inquiry.setAdmin(admin);
        inquiry.setUpdatedAt(LocalDateTime.now());

        eventPublisher.publishEvent(new InquiryAnsweredEvent(this, inquiry));
    }

    /** 관리자 권한 체크 */
    private User validateAdmin(Long adminId) {
        User admin = userFinder.getById(adminId);
        if (admin.getUserRole() != Role.ADMIN) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        return admin;
    }

    /** 문의 존재 여부 및 답변 가능 여부 체크 */
    private Inquiry validateInquiryAvailable(Long inquiryId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INQUIRY_NOT_FOUND));

        if (inquiry.getAnswerStatus() == Status.Y) {
            throw new BusinessException(ErrorCode.ALREADY_ANSWERED);
        }

        return inquiry;
    }
}
