package com.linkee.linkeeapi.comment.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentRequestDto {
    private Long userId;
    @NotBlank
    private String commentContent;
}