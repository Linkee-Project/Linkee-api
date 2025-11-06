package com.linkee.linkeeapi.inquiry.command.application.controller;

import com.linkee.linkeeapi.inquiry.command.application.dto.request.CreateInquiryRequestDto;
import com.linkee.linkeeapi.inquiry.command.application.dto.request.UpdateInquiryAnswerRequestDto;
import com.linkee.linkeeapi.inquiry.command.application.service.InquiryCommandService;
import com.linkee.linkeeapi.user.command.infrastructure.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> createInquiry(@RequestBody CreateInquiryRequestDto request){
        inquiryService.createInquiry(request);
        return ResponseEntity.ok("문의사항 생성 완료");
    }

    //update
    @PatchMapping("/answer")
    public ResponseEntity<String> updateInquiryAnswer(
            @RequestBody UpdateInquiryAnswerRequestDto request) {

        inquiryService.updateInquiryAnswer(request);
        return ResponseEntity.ok("답변 등록 완료");
    }
}
