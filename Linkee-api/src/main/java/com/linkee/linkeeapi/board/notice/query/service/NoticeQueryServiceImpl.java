package com.linkee.linkeeapi.board.notice.query.service;

import com.linkee.linkeeapi.board.notice.command.infrastructure.repository.NoticeRepository;
import com.linkee.linkeeapi.board.notice.query.dto.response.NoticeDetailResponseDto;
import com.linkee.linkeeapi.board.notice.query.dto.response.NoticeListResponseDto;
import com.linkee.linkeeapi.board.notice.query.mapper.NoticeMapper;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.users.command.application.service.util.UserFinder;
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
    public PageResponse<NoticeListResponseDto> getNoticeList(String active,int page, Integer size) {
        int pageSize = (size != null) ? size : 10;
        int offset = page * pageSize;

        List<NoticeListResponseDto> list;
        int total;

        if (active == null) {
            // 전체 조회(관리자)
            list = noticeMapper.findAll(offset, pageSize);
            total = noticeMapper.countAll();
        } else {
            // 활성/비활성 필터 조회
            list = noticeMapper.findByActive(active, offset, pageSize);
            total = noticeMapper.countByActive(active);
        }
        return PageResponse.from(list, page, pageSize, total);
    }

    //공지사항 상세 조회
    @Override
    public NoticeDetailResponseDto getNoticeDetail(Long noticeId) {
        if (noticeId == null) {
            throw new BusinessException(ErrorCode.INVALID_NOTICE_ID);
        }

        //조회수 증가
        noticeMapper.increaseViewCount(noticeId);

        //Id로 검색해서 상세조회
        NoticeDetailResponseDto notice = noticeMapper.findById(noticeId);

        if (notice == null) {
            throw new BusinessException(ErrorCode.NOTICE_NOT_FOUND);
        }
        return notice;
    }


}
