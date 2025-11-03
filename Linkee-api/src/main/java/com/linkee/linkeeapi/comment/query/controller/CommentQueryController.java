package com.linkee.linkeeapi.comment.query.controller;

import com.linkee.linkeeapi.comment.query.dto.CommentListResponseDto;
import com.linkee.linkeeapi.comment.query.service.CommentQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentQueryController {

    private final CommentQueryService service;

    /** 문제별 전체 댓글 조회 (부모+자식 포함) */
    @GetMapping("/questions/{questionId}")
    public ResponseEntity<List<CommentListResponseDto>> listByQuestion(@PathVariable Long questionId) {
        return ResponseEntity.ok(service.listAllForQuestion(questionId));
    }

    /** 특정 부모 댓글의 대댓글 조회 */
    @GetMapping("/parents/{parentId}/children")
    public ResponseEntity<List<CommentListResponseDto>> listChildren(@PathVariable Long parentId) {
        return ResponseEntity.ok(service.listChildren(parentId));
    }
}
