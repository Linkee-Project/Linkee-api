package com.linkee.linkeeapi.notice.query.controller;

import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.notice.query.dto.response.NoticeDetailResponseDto;
import com.linkee.linkeeapi.notice.query.dto.response.NoticeListResponseDto;
import com.linkee.linkeeapi.notice.query.service.NoticeQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notice")
@Tag(name = "공지", description = "공지사항 관리 API")
public class NoticeQueryController {

    private final NoticeQueryService noticeQueryService;

    //공지사항 목록 조회
    @GetMapping
    public ResponseEntity<PageResponse<NoticeListResponseDto>> getNoticeList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size
    ){
        PageResponse<NoticeListResponseDto> response =
                noticeQueryService.getNoticeList(page, size);

        return ResponseEntity.ok(response);
    }

    //공지사항 상세 조회
    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeDetailResponseDto> getNoticeDetail(@PathVariable Long noticeId){
        NoticeDetailResponseDto detailNotice = noticeQueryService.getNoticeDetail(noticeId);

        return ResponseEntity.ok(detailNotice);
    }
}
