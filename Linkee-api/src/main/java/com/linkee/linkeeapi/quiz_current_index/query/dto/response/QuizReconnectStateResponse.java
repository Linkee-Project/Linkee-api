package com.linkee.linkeeapi.quiz_current_index.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizReconnectStateResponse {

    private Long quizRoomId;
    private String roomStatus;
    private Integer roomQuizLimit;
    private Integer joinedCount;
    private Integer currentQuizIndex;

}
