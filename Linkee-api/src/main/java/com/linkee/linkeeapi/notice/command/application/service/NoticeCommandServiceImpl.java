package com.linkee.linkeeapi.notice.command.application.service;

import com.linkee.linkeeapi.common.enums.Role;
import com.linkee.linkeeapi.notice.command.application.dto.request.CreateNoticeRequestDto;
import com.linkee.linkeeapi.notice.command.application.dto.request.UpdateNoticeRequestDto;
import com.linkee.linkeeapi.notice.command.domain.aggregate.entity.Notice;
import com.linkee.linkeeapi.notice.command.infrastructure.repository.NoticeRepository;
import com.linkee.linkeeapi.notice.query.mapper.NoticeMapper;
import com.linkee.linkeeapi.user.model.entity.User;
import com.linkee.linkeeapi.user.service.util.UserFinder;
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
    public void createNotice(CreateNoticeRequestDto request) {

        User adminUser = userFinder.getById(request.getAdminId());

        if (adminUser.getUserRole() == Role.ADMIN) {
            Notice notice = modelMapper.map(request, Notice.class);
            notice.assignAdmin(adminUser);
            noticeRepository.save(notice);

        } else {
            throw new IllegalStateException("관리자만 공지사항 등록 가능");
        }

    }


    //공지사항 수정
    @Transactional
    @Override
    public void updateNotice(UpdateNoticeRequestDto request) {
        Notice notice = noticeRepository.findById(request.getNoticeId())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 공지사항입니다."));

        User adminUser = userFinder.getById(request.getAdminId());
        if (adminUser.getUserRole() != Role.ADMIN) {
            throw new IllegalStateException("관리자만 공지사항 수정 가능");
        }

        notice.updateNotice(request.getNoticeTitle(), request.getNoticeContent());

    }

    //공지사항 삭제
    @Override
    @Transactional
    public void deleteNotice(Long noticeId, Long adminId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 공지사항입니다."));

        User adminUser = userFinder.getById(adminId);
        if (adminUser.getUserRole() != Role.ADMIN) {
            throw new IllegalStateException("관리자만 삭제 가능");
        }

        notice.deleteNotice(); // isDeleted = Y
        noticeRepository.save(notice); // updatedAt 자동 갱신
    }

}
