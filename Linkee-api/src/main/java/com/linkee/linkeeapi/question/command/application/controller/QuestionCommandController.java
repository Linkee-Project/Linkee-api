package com.linkee.linkeeapi.question.command.application.controller;

import com.linkee.linkeeapi.common.model.CustomUser;
import com.linkee.linkeeapi.question.command.application.dto.request.CreateQuestionRequestDto;
import com.linkee.linkeeapi.question.command.application.dto.request.UpdateQuestionRequestDto;
import com.linkee.linkeeapi.question.command.application.service.QuestionCommandService;
import com.linkee.linkeeapi.users.command.infrastructure.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "문제", description = "문제 게시판 및 댓글 관리 API")
@RequestMapping("/api/v1")
public class QuestionCommandController {

    private final QuestionCommandService questionCommandService;
    private final UserRepository userRepository;

    // 문제 등록
    @PostMapping("/question/questions/new")
    public ResponseEntity<String> create(@Valid @RequestBody CreateQuestionRequestDto request,
                                         @AuthenticationPrincipal CustomUser user) {
        Long userId = user.getUserId();
        questionCommandService.createQuestion(request, userId);
        return ResponseEntity.ok("문제 등록 완료");
    }

    //문제 수정
    @PatchMapping("/question/questions/{questionId}")
    public ResponseEntity<String> update(@PathVariable Long questionId,
                                         @Valid @RequestBody UpdateQuestionRequestDto request,
                                         @AuthenticationPrincipal CustomUser user) {
        Long userId = user.getUserId();
        questionCommandService.updateQuestion(questionId, request,userId);
        return ResponseEntity.ok("문제 수정 완료");
    }

    //문제 삭제
    @DeleteMapping("/question/questions/{questionId}")
    public ResponseEntity<String> delete(@PathVariable Long questionId,
                                         @AuthenticationPrincipal CustomUser user) {
        Long userId = user.getUserId();
        questionCommandService.deleteQuestion(questionId, userId);
        return ResponseEntity.ok("문제 삭제 완료");
    }

    /* 관리자 - 문제 검증 변경 */
    @PostMapping("/admin/question/questions/{questionId}/verify")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> verify(
            @PathVariable Long questionId,
            @AuthenticationPrincipal CustomUser admin
    ) {
        Long adminId = admin.getUserId();
        questionCommandService.verifyQuestion(questionId, adminId);
        return ResponseEntity.ok("문제 검증 완료");
    }

    // 관리자용: 문제 소프트 삭제
    @PatchMapping("/admin/question/questions/{questionId}/delete") // PATCH 매핑으로 변경
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> adminSoftDeleteQuestion( // 메서드 이름 변경
            @PathVariable Long questionId,
            @AuthenticationPrincipal CustomUser admin
    ) {
        Long adminId = admin.getUserId();
        questionCommandService.adminDeleteQuestion(questionId, adminId); // 기존의 소프트 삭제 메서드 호출
        return ResponseEntity.ok("문제 소프트 삭제 완료");
    }

}
