package com.linkee.linkeeapi.comment.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateCommentRequestDto {
    private Long userId;

    //null이면 루트 댓글(부모)
    private Long parentCommentId;

    @NotBlank
    @Size(max = 1000)
    private String commentContent;

}