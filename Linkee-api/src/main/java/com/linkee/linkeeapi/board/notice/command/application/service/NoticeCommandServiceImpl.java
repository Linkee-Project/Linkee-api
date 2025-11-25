package com.linkee.linkeeapi.board.notice.command.application.service;

import com.linkee.linkeeapi.board.notice.command.application.dto.request.CreateNoticeRequestDto;
import com.linkee.linkeeapi.board.notice.command.application.dto.request.UpdateNoticeRequestDto;
import com.linkee.linkeeapi.board.notice.command.domain.aggregate.entity.Notice;
import com.linkee.linkeeapi.board.notice.command.infrastructure.repository.NoticeRepository;
import com.linkee.linkeeapi.board.notice.query.mapper.NoticeMapper;
import com.linkee.linkeeapi.common.enums.Role;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.common.model.CustomUser;
import com.linkee.linkeeapi.users.command.application.service.util.UserFinder;
import com.linkee.linkeeapi.users.command.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeCommandServiceImpl implements NoticeCommandService {

    private final UserFinder userFinder;
    private final ModelMapper modelMapper;
    private final NoticeRepository noticeRepository;
    private final NoticeMapper noticeMapper;

    //공지사항 등록
    //관리자만 등록 가능
    @Override
    public void createNotice(CustomUser customUser, CreateNoticeRequestDto request) {
        User adminUser = userFinder.getById(customUser.getUserId());

        if (adminUser.getUserRole() == Role.ADMIN) {
            Notice notice = modelMapper.map(request, Notice.class);
            notice.assignAdmin(adminUser);
            noticeRepository.save(notice);
        } else {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
    }



    // ✅ 공지사항 수정 (관리자만)
    @Override
    @Transactional
    public void updateNotice(CustomUser customUser,Long noticeId, UpdateNoticeRequestDto request) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOTICE_NOT_FOUND));

        User adminUser = userFinder.getById(customUser.getUserId());

        if (adminUser.getUserRole() != Role.ADMIN) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        notice.updateNotice(request.getNoticeTitle(), request.getNoticeContent());

        if (request.getIsActive() != null) {
            notice.updateActive(request.getIsActive());
        }
        noticeRepository.save(notice);

    }

    // ✅ 공지사항 삭제 (관리자만)
    @Override
    @Transactional
    public void deleteNotice(CustomUser customUser, Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOTICE_NOT_FOUND));

        User adminUser = userFinder.getById(customUser.getUserId());
        if (adminUser.getUserRole() != Role.ADMIN) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        notice.deleteNotice();
        noticeRepository.save(notice);
    }
}
