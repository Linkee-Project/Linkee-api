package com.linkee.linkeeapi.notice.query.service;

import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.notice.command.infrastructure.repository.NoticeRepository;
import com.linkee.linkeeapi.notice.query.dto.response.NoticeDetailResponseDto;
import com.linkee.linkeeapi.notice.query.dto.response.NoticeListResponseDto;
import com.linkee.linkeeapi.notice.query.mapper.NoticeMapper;
import com.linkee.linkeeapi.user.command.application.service.util.UserFinder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeQueryServiceImpl implements NoticeQueryService {

    private final UserFinder userFinder;
    private final ModelMapper modelMapper;
    private final NoticeRepository noticeRepository;
    private final NoticeMapper noticeMapper;


    //공지사항 목록 조회
    //조회수 구현 필요
    @Override
    public PageResponse<NoticeListResponseDto> getNoticeList(int page, Integer size) {
        int pageSize = (size != null) ? size : 10;
        int offset = page * pageSize;

        List<NoticeListResponseDto> notices = noticeMapper.findAll(offset, pageSize);
        int total = noticeMapper.countAll();

        return PageResponse.from(notices, page, pageSize, total);
    }

    //공지사항 상세 조회
    @Override
    public NoticeDetailResponseDto getNoticeDetail(Long noticeId) {
        if (noticeId == null) {
            throw new BusinessException(ErrorCode.INVALID_NOTICE_ID);
        }

        //조회수 증가
        int updated = noticeMapper.increaseViewCount(noticeId);
        if (updated == 0) {
            throw new BusinessException(ErrorCode.NOTICE_NOT_FOUND);
        }

        //Id로 검색해서 상세조회
        NoticeDetailResponseDto notice = noticeMapper.findById(noticeId);

        if (notice == null) {
            throw new BusinessException(ErrorCode.NOTICE_NOT_FOUND);
        }
        return notice;
    }


}
