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
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user.command.application.service.util.UserFinder;
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
    public void createInquiry(CreateInquiryRequestDto request) {
        if (request.getUserId() == null) {
            throw new BusinessException(ErrorCode.INVALID_USER_ID);
        }
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
                .user(userFinder.getById(request.getUserId()))
                .admin(null)
                .answerStatus(Status.N)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();


        inquiryRepository.save(inquiry);

    }

    //Update -답변등록
    @Override
    @Transactional(readOnly = false)
    public void updateInquiryAnswer(UpdateInquiryAnswerRequestDto request) {

        if (request.getInquiryId() == null) {
            throw new BusinessException(ErrorCode.INVALID_INQUIRY_ID);
        }

        //관리자 조회
        User adminUser = userFinder.getById(request.getAdminId()); // 없는 경우 INVALID_ADMIN_ID로 처리
        if (adminUser.getUserRole() != Role.ADMIN) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        //문의 조회
        Inquiry inquiry = inquiryRepository.findById(request.getInquiryId())
                .orElseThrow(() -> new BusinessException(ErrorCode.INQUIRY_NOT_FOUND));

        // 이미 답변된 문의인지 확인
        if (inquiry.getAnswerStatus() == Status.Y) {
            throw new BusinessException(ErrorCode.ALREADY_ANSWERED);
        }

        //답변 등록
        //mapper 사용 x (mapper로 들어갈게 answerContent 뿐임)
        inquiry.setAnswerContent(request.getAnswerContent());
        inquiry.setAnswerStatus(Status.Y);
        inquiry.setAdmin(adminUser);
        inquiry.setUpdatedAt(LocalDateTime.now());

        inquiryRepository.save(inquiry);

        // 이벤트 발생 추가 (알림)
        eventPublisher.publishEvent(new InquiryAnsweredEvent(this, inquiry));
    }
}
