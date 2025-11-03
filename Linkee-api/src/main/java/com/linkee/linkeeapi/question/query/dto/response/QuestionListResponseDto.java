package com.linkee.linkeeapi.question.query.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionListResponseDto {
    private Long questionId;
    private String questionTitle;
    private String categoryName;
    private String userNickname;
    private Integer viewCount;
    private LocalDateTime createdAt;



}
