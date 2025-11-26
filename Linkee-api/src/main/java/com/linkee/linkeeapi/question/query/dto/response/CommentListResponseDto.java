package com.linkee.linkeeapi.question.query.dto.response;

import com.linkee.linkeeapi.common.enums.Status;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentListResponseDto {
    private Long commentId;
    private Long parentCommentId;     // null이면 최상위 댓글
    private Long questionId;
    private Long userId;
    private String userNickname;
    private String commentContent;
    private Status isDeleted;
    private LocalDateTime createdAt;
    private String userEmail;


    private Integer childCount;       // 대댓글 수
}
