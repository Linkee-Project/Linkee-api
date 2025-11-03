package com.linkee.linkeeapi.inquiry.command.application.service;

import com.linkee.linkeeapi.inquiry.command.application.dto.request.CreateInquiryRequestDto;
import com.linkee.linkeeapi.inquiry.command.application.dto.request.UpdateInquiryAnswerRequestDto;

public interface InquiryCommandService {

    //CREATE
    void createInquiry(CreateInquiryRequestDto createInquiryRequestDto);

    //UPDATE
     void updateInquiryAnswer(UpdateInquiryAnswerRequestDto request);


}
