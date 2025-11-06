package com.linkee.linkeeapi.quiz.domain.dto.command_dto.response;

import com.linkee.linkeeapi.quiz.domain.dto.command_dto.QuizMessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizWebSocketResponse {
    private QuizMessageType type;
    private Boolean success;
    private String message;
    private Object data;  // 문제 정보, 결과 등 데이터
}