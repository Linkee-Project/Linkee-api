package com.linkee.linkeeapi.board.inquiry.command.application.service;

import com.linkee.linkeeapi.board.inquiry.command.application.dto.request.CreateInquiryRequestDto;
import com.linkee.linkeeapi.board.inquiry.command.application.dto.request.UpdateInquiryAnswerRequestDto;
import com.linkee.linkeeapi.common.model.CustomUser;

public interface InquiryCommandService {

    //CREATE
    void createInquiry(Long userId,CreateInquiryRequestDto createInquiryRequestDto);

    //UPDATE
     void updateInquiryAnswer(CustomUser customUser,UpdateInquiryAnswerRequestDto request);


}
