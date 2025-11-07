package com.linkee.linkeeapi.inquiry.query.service;


import com.linkee.linkeeapi.common.enums.Role;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.inquiry.query.dto.response.InquiryResponseDto;
import com.linkee.linkeeapi.inquiry.query.mapper.InquiryMapper;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user.command.application.service.util.UserFinder;
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


    //READ - 전체 목록조회
    @Override
    public PageResponse<InquiryResponseDto> getInquiryList(int page, Integer size, User currentUser) {

        int pageSize = (size != null) ? size : 10;
        int offset = page * pageSize;

        List<InquiryResponseDto> inquiries;
        int total;

        if (currentUser == null) {
            throw new BusinessException(ErrorCode.INVALID_USER_ID);
        }


        if (currentUser.getUserRole() == Role.ADMIN) {
            inquiries = inquiryMapper.findAll(offset, pageSize);
            total = inquiryMapper.countAll();
        } else {
            inquiries = inquiryMapper.findByUserId(currentUser.getUserId(), offset, pageSize);
            total = inquiryMapper.countByUserId(currentUser.getUserId());
        }

        return PageResponse.from(inquiries, page, pageSize, total);
    }

    
}
