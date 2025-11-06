package com.linkee.linkeeapi.quiz.domain.dto.command_dto.response.data;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizResultData {
    // 정답 정보
    private Long correctOptionId;
    private String correctOptionText;

    // 각 참가자별 정오답
    private List<QuizUserResult> userResults;

}