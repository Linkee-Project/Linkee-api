package com.linkee.linkeeapi.board.inquiry.command.application.controller;

import com.linkee.linkeeapi.board.inquiry.command.application.dto.request.CreateInquiryRequestDto;
import com.linkee.linkeeapi.board.inquiry.command.application.dto.request.UpdateInquiryAnswerRequestDto;
import com.linkee.linkeeapi.board.inquiry.command.application.service.InquiryCommandService;

import com.linkee.linkeeapi.common.model.CustomUser;
import com.linkee.linkeeapi.users.command.infrastructure.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/inquiry")
@Tag(name = "문의", description = "문의 및 답변 관리 API")
public class InquiryCommandController {
    private final InquiryCommandService inquiryService;
    private final UserRepository userRepository;

    //create
    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<String> createInquiry(
            @AuthenticationPrincipal CustomUser customUser,
            @RequestBody CreateInquiryRequestDto request){
        inquiryService.createInquiry(customUser.getUserId(),request);
        return ResponseEntity.ok("문의사항 생성 완료");
    }

    //update
    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/answer")
    public ResponseEntity<String> updateInquiryAnswer(
            @AuthenticationPrincipal CustomUser customUser,
            @RequestBody UpdateInquiryAnswerRequestDto request) {

        request.setAdminId(customUser.getUserId());
        inquiryService.updateInquiryAnswer(customUser,request);
        return ResponseEntity.ok("답변 등록 완료");
    }
}
