package com.linkee.linkeeapi.quiz_current_index.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizCurrentIndexResponse {

    private Long quizRoomId;
    private Integer currentQuizIndex;
    private Long roomQuestionId;
}
