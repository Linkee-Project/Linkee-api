package com.linkee.linkeeapi.notice.controller;

import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.notice.model.dto.request.CreateNoticeRequestDto;
import com.linkee.linkeeapi.notice.model.dto.request.DeleteNoticeRequestDto;
import com.linkee.linkeeapi.notice.model.dto.request.UpdateNoticeRequestDto;
import com.linkee.linkeeapi.notice.model.dto.response.NoticeDetailResponseDto;
import com.linkee.linkeeapi.notice.model.dto.response.NoticeListResponseDto;
import com.linkee.linkeeapi.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notice")
public class NoticeController {

    private final NoticeService noticeService;

    //공지사항 등록
    //admin아니면 오류 던지기
    @PostMapping
    public ResponseEntity<String> createNotice(@RequestBody CreateNoticeRequestDto request){
        noticeService.createNotice(request);
        return ResponseEntity.ok("공지사항 생성 완료");
    }

    //공지사항 목록 조회
    @GetMapping
    public ResponseEntity<PageResponse<NoticeListResponseDto>> getNoticeList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size
    ){
        PageResponse<NoticeListResponseDto> response =
                noticeService.getNoticeList(page, size);

        return ResponseEntity.ok(response);
    }

    //공지사항 상세 조회
    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeDetailResponseDto> getNoticeDetail(@PathVariable Long noticeId){
        NoticeDetailResponseDto detailNotice = noticeService.getNoticeDetail(noticeId);

        return ResponseEntity.ok(detailNotice);
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
