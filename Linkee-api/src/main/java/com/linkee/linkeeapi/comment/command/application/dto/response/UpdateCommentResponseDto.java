package com.linkee.linkeeapi.comment.command.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class UpdateCommentResponseDto {
    private Long commentId;
    private String commentContent;
    private LocalDateTime updatedAt;
}