package com.linkee.linkeeapi.question.command.application.controller;

import com.linkee.linkeeapi.common.model.CustomUser;
import com.linkee.linkeeapi.question.command.application.dto.request.CreateCommentRequestDto;
import com.linkee.linkeeapi.question.command.application.dto.request.UpdateCommentRequestDto;
import com.linkee.linkeeapi.question.command.application.dto.response.CreateCommentResponseDto;
import com.linkee.linkeeapi.question.command.application.dto.response.UpdateCommentResponseDto;
import com.linkee.linkeeapi.question.command.application.service.CommentCommandService;
import com.linkee.linkeeapi.question.command.application.service.CommentCommandServiceImpl;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "문제", description = "문제 게시판 및 댓글 관리 API")
public class CommentCommandController {

    private final CommentCommandService commentCommandService;


    // 댓글 등록
    @PostMapping("/question/questions/{questionId}/comments")
    public ResponseEntity<CreateCommentResponseDto> create(
            @PathVariable Long questionId,
            @AuthenticationPrincipal CustomUser user,
            @Valid @RequestBody CreateCommentRequestDto request) {
        Long userId = user.getUserId();
        CreateCommentResponseDto response = commentCommandService.createComment(questionId, userId, request);
        return ResponseEntity.ok(response);
    }
    //댓글 수정
    @PatchMapping("/question/questions/{questionId}/comments/{commentId}")
    public ResponseEntity<UpdateCommentResponseDto> update(
            @PathVariable Long questionId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUser user,
            @Valid @RequestBody UpdateCommentRequestDto request
    ) {
        Long userId = user.getUserId();
        UpdateCommentResponseDto response =
                commentCommandService.updateComment(questionId, commentId, userId, request);
        return ResponseEntity.ok(response);
    }

    // 댓글 삭제
    @DeleteMapping("/question/questions/{questionId}/comments/{commentId}")
    public ResponseEntity<String> delete(
            @PathVariable Long questionId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUser user
    ) {
        Long userId = user.getUserId();
        commentCommandService.deleteComment(questionId, commentId, userId);
        return ResponseEntity.ok("댓글 삭제 완료");
    }

    /* 관리자 - 댓글 삭제 */
    @DeleteMapping("/admin/question/questions/{questionId}/comments/{commentId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteAdmin(
            @PathVariable Long questionId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUser admin
    ){
        Long adminId = admin.getUserId();
        commentCommandService.adminDeleteComment(questionId, commentId, adminId);
        return ResponseEntity.ok("댓글 삭제 완료 (관리자)");
    }

}
