package com.linkee.linkeeapi.board.notice.command.application.controller;


import com.linkee.linkeeapi.board.notice.command.application.dto.request.CreateNoticeRequestDto;
import com.linkee.linkeeapi.board.notice.command.application.dto.request.UpdateNoticeRequestDto;
import com.linkee.linkeeapi.board.notice.command.application.service.NoticeCommandService;
import com.linkee.linkeeapi.common.model.CustomUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notice")
@Tag(name = "공지", description = "공지사항 관리 API")
public class NoticeCommandController {

    private final NoticeCommandService noticeService;

    //공지사항 등록(관리자 전용)
    //admin아니면 오류 던지기
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<String> createNotice(@AuthenticationPrincipal CustomUser customUser,
                                               @RequestBody CreateNoticeRequestDto request) {
        noticeService.createNotice(customUser, request);
        return ResponseEntity.ok("공지사항 생성 완료 (관리자 ID: " + customUser.getUserId() + ")");
    }

    //공지사항 수정 (관리자 전용)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{noticeId}")
    public ResponseEntity<Void> updateNotice(
            @AuthenticationPrincipal CustomUser customUser,
            @PathVariable Long noticeId,
            @RequestBody UpdateNoticeRequestDto request) {

        request.setNoticeId(noticeId);
        noticeService.updateNotice(customUser, request);
        return ResponseEntity.ok().build();
    }

    // ✅ 공지사항 삭제 (관리자 전용)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{noticeId}/delete")
    public ResponseEntity<Void> deleteNotice(
            @AuthenticationPrincipal CustomUser customUser,
            @PathVariable Long noticeId) {

        noticeService.deleteNotice(customUser, noticeId);
        return ResponseEntity.ok().build();
    }
}