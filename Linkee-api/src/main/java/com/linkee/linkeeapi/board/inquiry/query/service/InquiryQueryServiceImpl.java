package com.linkee.linkeeapi.board.inquiry.query.service;


import com.linkee.linkeeapi.board.inquiry.command.domain.aggregate.Inquiry;
import com.linkee.linkeeapi.board.inquiry.command.infrastructure.repository.InquiryRepository;
import com.linkee.linkeeapi.board.inquiry.query.dto.response.InquiryResponseDto;
import com.linkee.linkeeapi.board.inquiry.query.mapper.InquiryMapper;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.common.model.CustomUser;
import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.users.command.application.service.util.UserFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryQueryServiceImpl implements InquiryQueryService {
    private final UserFinder userFinder;
    private final InquiryMapper inquiryMapper;
    private final InquiryRepository inquiryRepository;

    //READ - 전체 목록조회
    @Override
    public PageResponse<InquiryResponseDto> getInquiryList(int page, Integer size, String answerStatus, CustomUser customUser) {

        if (customUser == null) {
            throw new BusinessException(ErrorCode.INVALID_USER_ID);
        }

        int pageSize = (size != null) ? size : 10;
        int offset = page * pageSize;

        List<InquiryResponseDto> inquiries;
        int total;

        // 관리자일 때
        if ("ADMIN".equals(customUser.getRole())) {
            inquiries = inquiryMapper.findAll(answerStatus, offset, pageSize);
            total = inquiryMapper.countAll(); // 필터된 개수로 바꿀 수도 있음
        }
        else { // 일반 유저일 때
            inquiries = inquiryMapper.findByUserId(customUser.getUserId(), offset, pageSize);
            total = inquiryMapper.countByUserId(customUser.getUserId());
        }

        return PageResponse.from(inquiries, page, pageSize, total);
    }

    @Override
    public InquiryResponseDto getInquiryDetail(Long inquiryId, CustomUser customUser) {

        if (customUser == null) {
            throw new BusinessException(ErrorCode.INVALID_USER_ID);
        }

        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INQUIRY_NOT_FOUND));

        // 일반 사용자일 경우 본인 게시글만 조회 가능
        boolean isAdmin = "ADMIN".equals(customUser.getRole());
        if (!isAdmin) {
            if (!inquiry.getUser().getUserId().equals(customUser.getUserId())) {
                throw new BusinessException(ErrorCode.NO_ACCESS_PERMISSION);
            }
        }

        return InquiryResponseDto.builder()
                .inquiryId(inquiry.getInquiryId())
                .inquiryTitle(inquiry.getInquiryTitle())
                .inquiryContent(inquiry.getInquiryContent())
                .createdAt(inquiry.getCreatedAt())
                .updatedAt(inquiry.getUpdatedAt())
                .answerContent(inquiry.getAnswerContent())
                .answerStatus(inquiry.getAnswerStatus())
                .userId(inquiry.getUser().getUserId())
                .adminId(inquiry.getAdmin() != null ? inquiry.getAdmin().getUserId() : null)
                .userNickname(inquiry.getUser().getUserNickname())
                .build();
    }

}
