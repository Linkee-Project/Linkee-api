package com.linkee.linkeeapi.question.command.application.service;

import com.linkee.linkeeapi.question.command.application.dto.request.CreateCommentRequestDto;
import com.linkee.linkeeapi.question.command.application.dto.request.UpdateCommentRequestDto;
import com.linkee.linkeeapi.question.command.application.dto.response.CreateCommentResponseDto;
import com.linkee.linkeeapi.question.command.application.dto.response.UpdateCommentResponseDto;

public interface CommentCommandService {
    CreateCommentResponseDto createComment(Long questionId, Long userId, CreateCommentRequestDto req);
    UpdateCommentResponseDto updateComment(Long questionId, Long commentId, Long userId, UpdateCommentRequestDto request);
    void deleteComment(Long questionId, Long commentId, Long userId);
}