package com.linkee.linkeeapi.notice.command.application.service;


import com.linkee.linkeeapi.notice.command.application.dto.request.CreateNoticeRequestDto;
import com.linkee.linkeeapi.notice.command.application.dto.request.UpdateNoticeRequestDto;

public interface NoticeCommandService {

    //CREATE
    void createNotice(CreateNoticeRequestDto request);

    //UPDATE
    void updateNotice(UpdateNoticeRequestDto request);

    //DELETE
    void deleteNotice(Long noticeId, Long adminId);
}
