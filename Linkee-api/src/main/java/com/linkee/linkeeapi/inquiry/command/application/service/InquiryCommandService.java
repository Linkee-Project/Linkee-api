package com.linkee.linkeeapi.inquiry.command.application.service;

import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.inquiry.command.application.dto.request.CreateInquiryRequestDto;
import com.linkee.linkeeapi.inquiry.command.application.dto.request.UpdateInquiryAnswerRequestDto;
import com.linkee.linkeeapi.user.model.entity.User;

public interface InquiryCommandService {

    //CREATE
    void createInquiry(CreateInquiryRequestDto createInquiryRequestDto);

    //UPDATE
     void updateInquiryAnswer(UpdateInquiryAnswerRequestDto request);


}
