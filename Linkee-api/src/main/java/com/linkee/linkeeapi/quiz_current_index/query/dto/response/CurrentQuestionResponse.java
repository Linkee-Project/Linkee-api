package com.linkee.linkeeapi.quiz_current_index.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentQuestionResponse {
    private Long questionId;
    private String questionContent;
    private String option1; // 답 보기 4개
    private String option2;
    private String option3;
    private String option4;

}
