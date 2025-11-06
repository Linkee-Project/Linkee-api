package com.linkee.linkeeapi.comment.command.application.controller;

import com.linkee.linkeeapi.comment.command.application.dto.request.CreateCommentRequestDto;
import com.linkee.linkeeapi.comment.command.application.dto.request.UpdateCommentRequestDto;
import com.linkee.linkeeapi.comment.command.application.dto.response.CreateCommentResponseDto;
import com.linkee.linkeeapi.comment.command.application.dto.response.UpdateCommentResponseDto;
import com.linkee.linkeeapi.comment.command.application.service.CommentCommandService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/questions/{questionId}/comments")
@Tag(name = "문제", description = "문제 게시판 및 댓글 관리 API")
public class CommentCommandController {

    private final CommentCommandService commentCommandService;


    // 댓글 등록
    @PostMapping
    public ResponseEntity<CreateCommentResponseDto> create(
            @PathVariable Long questionId,
            @RequestParam Long userId,
            @Valid @RequestBody CreateCommentRequestDto request) {

        CreateCommentResponseDto response = commentCommandService.createComment(questionId, userId, request);
        return ResponseEntity.ok(response);
    }
    //댓글 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<UpdateCommentResponseDto> update(
            @PathVariable Long questionId,
            @PathVariable Long commentId,
            @RequestParam Long userId,
            @Valid @RequestBody UpdateCommentRequestDto request
    ) {
        UpdateCommentResponseDto response =
                commentCommandService.updateComment(questionId, commentId, userId, request);
        return ResponseEntity.ok(response);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> delete(
            @PathVariable Long questionId,
            @PathVariable Long commentId,
            @RequestParam Long userId
    ) {
        commentCommandService.deleteComment(questionId, commentId, userId);
        return ResponseEntity.ok("댓글 삭제 완료");
    }

}
