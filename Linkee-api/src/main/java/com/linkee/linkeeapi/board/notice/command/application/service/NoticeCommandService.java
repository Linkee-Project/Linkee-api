package com.linkee.linkeeapi.board.notice.command.application.service;


import com.linkee.linkeeapi.board.notice.command.application.dto.request.CreateNoticeRequestDto;
import com.linkee.linkeeapi.board.notice.command.application.dto.request.UpdateNoticeRequestDto;
import com.linkee.linkeeapi.common.model.CustomUser;

public interface NoticeCommandService {

    //CREATE
    void createNotice(CustomUser customUser, CreateNoticeRequestDto request);

    //UPDATE
    void updateNotice(CustomUser customUser,Long noticeId, UpdateNoticeRequestDto request);

    //DELETE
    void deleteNotice(CustomUser customUser, Long noticeId);
}
