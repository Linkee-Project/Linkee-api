package com.linkee.linkeeapi.question.command.application.controller;

import com.linkee.linkeeapi.common.model.CustomUser;
import com.linkee.linkeeapi.question.command.application.dto.request.CreateQuestionRequestDto;
import com.linkee.linkeeapi.question.command.application.dto.request.UpdateQuestionRequestDto;
import com.linkee.linkeeapi.question.command.application.dto.request.VerifyQuestionRequestDto;
import com.linkee.linkeeapi.question.command.application.service.QuestionCommandService;
import com.linkee.linkeeapi.users.command.infrastructure.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "문제", description = "문제 게시판 및 댓글 관리 API")
@RequestMapping("/api/v1/question/questions")
public class QuestionCommandController {

    private final QuestionCommandService questionCommandService;
    private final UserRepository userRepository;

    // 문제 등록
    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody CreateQuestionRequestDto request,
                                         @AuthenticationPrincipal CustomUser user) {
        Long userId = user.getUserId();
        questionCommandService.createQuestion(request, userId);
        return ResponseEntity.ok("문제 등록 완료");
    }
    //문제 검증 변경(관리자)
    @PostMapping("/{id}/verify")
    public ResponseEntity<String> verify(
            @PathVariable Long id,
            @RequestBody VerifyQuestionRequestDto request
    ) {
        questionCommandService.verifyQuestion(id, request);
        return ResponseEntity.ok("문제 검증 완료");
    }

    //문제 수정
    @PatchMapping("/{questionId}")
    public ResponseEntity<String> update(@PathVariable Long questionId,
                                         @Valid @RequestBody UpdateQuestionRequestDto request,
                                         @AuthenticationPrincipal CustomUser user) {
        Long userId = user.getUserId();
        questionCommandService.updateQuestion(questionId, request,userId);
        return ResponseEntity.ok("문제 수정 완료");
    }

    //문제 삭제
    @DeleteMapping("/{questionId}")
    public ResponseEntity<String> delete(@PathVariable Long questionId,
                                         @AuthenticationPrincipal CustomUser user) {
        Long userId = user.getUserId();
        questionCommandService.deleteQuestion(questionId, userId);
        return ResponseEntity.ok("문제 삭제 완료");
    }

}
