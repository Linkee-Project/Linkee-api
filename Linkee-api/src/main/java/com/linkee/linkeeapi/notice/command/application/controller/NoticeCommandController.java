package com.linkee.linkeeapi.notice.command.application.controller;


import com.linkee.linkeeapi.notice.command.application.dto.request.CreateNoticeRequestDto;
import com.linkee.linkeeapi.notice.command.application.dto.request.DeleteNoticeRequestDto;
import com.linkee.linkeeapi.notice.command.application.dto.request.UpdateNoticeRequestDto;
import com.linkee.linkeeapi.notice.command.application.service.NoticeCommandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notice")
@Tag(name = "공지", description = "공지사항 관리 API")
public class NoticeCommandController {

    private final NoticeCommandService noticeService;

    //공지사항 등록
    //admin아니면 오류 던지기
    @PostMapping
    public ResponseEntity<String> createNotice(@RequestBody CreateNoticeRequestDto request){
        noticeService.createNotice(request);
        return ResponseEntity.ok("공지사항 생성 완료");
    }

    //공지사항 수정
    @PutMapping("/{noticeId}")
    public ResponseEntity<Void> updateNotice(@PathVariable Long noticeId,
                                             @RequestBody UpdateNoticeRequestDto request) {
        request.setNoticeId(noticeId);
        noticeService.updateNotice(request);
        return ResponseEntity.ok().build();

    }

    //공지사항 삭제
    @PatchMapping("/{noticeId}/delete")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long noticeId,
                                             @RequestBody DeleteNoticeRequestDto request) {
        noticeService.deleteNotice(noticeId, request.getAdminId());
        return ResponseEntity.ok().build();
    }
}
