package com.linkee.linkeeapi.board.inquiry.query.controller;

import com.linkee.linkeeapi.board.inquiry.query.dto.response.InquiryResponseDto;
import com.linkee.linkeeapi.board.inquiry.query.service.InquiryQueryService;
import com.linkee.linkeeapi.common.model.CustomUser;
import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.users.command.domain.entity.User;
import com.linkee.linkeeapi.users.command.infrastructure.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/board/inquiries")
@Tag(name = "문의", description = "문의 및 답변 관리 API")
public class InquiryQueryController {
    private final UserRepository userRepository;
    private final InquiryQueryService inquiryQueryService;

    //read
    @GetMapping()
    public ResponseEntity<PageResponse<InquiryResponseDto>> getInquiryList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String answerStatus,
            @AuthenticationPrincipal CustomUser customUser)
    {
        PageResponse<InquiryResponseDto> response =
                inquiryQueryService.getInquiryList(page, size, answerStatus, customUser);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InquiryResponseDto> getInquiryDetail(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal CustomUser customUser
    ) {
        InquiryResponseDto response = inquiryQueryService.getInquiryDetail(id, customUser);
        return ResponseEntity.ok(response);
    }
}
