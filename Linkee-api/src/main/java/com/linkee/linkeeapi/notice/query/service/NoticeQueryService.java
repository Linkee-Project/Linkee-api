package com.linkee.linkeeapi.notice.query.service;


import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.notice.query.dto.response.NoticeDetailResponseDto;
import com.linkee.linkeeapi.notice.query.dto.response.NoticeListResponseDto;

public interface NoticeQueryService {


    //READ
    PageResponse<NoticeListResponseDto> getNoticeList(int page, Integer size);

    NoticeDetailResponseDto getNoticeDetail(Long noticeId);

}
