package com.linkee.linkeeapi.board.inquiry.query.service;

import com.linkee.linkeeapi.board.inquiry.query.dto.response.InquiryResponseDto;
import com.linkee.linkeeapi.common.model.CustomUser;
import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.users.command.domain.entity.User;


public interface InquiryQueryService {

    //READ
    PageResponse<InquiryResponseDto> getInquiryList(int page, Integer size, String answerStatus, CustomUser customUser);

    InquiryResponseDto getInquiryDetail(Long inquiryId, CustomUser customUser);


}
