package com.linkee.linkeeapi.notice.service;

import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.notice.model.dto.request.CreateNoticeRequestDto;
import com.linkee.linkeeapi.notice.model.dto.request.UpdateNoticeRequestDto;
import com.linkee.linkeeapi.notice.model.dto.response.NoticeDetailResponseDto;
import com.linkee.linkeeapi.notice.model.dto.response.NoticeListResponseDto;

public interface NoticeService {

    //CREATE
    void createNotice(CreateNoticeRequestDto request);

    //READ
    PageResponse<NoticeListResponseDto> getNoticeList(int page, Integer size);

    NoticeDetailResponseDto getNoticeDetail(Long noticeId);

    //UPDATE
    void updateNotice(UpdateNoticeRequestDto request);

    //DELETE
    void deleteNotice(Long noticeId, Long adminId);
}
