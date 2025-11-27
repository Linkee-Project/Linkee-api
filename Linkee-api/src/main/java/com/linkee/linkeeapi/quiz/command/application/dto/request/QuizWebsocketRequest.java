package com.linkee.linkeeapi.quiz.command.application.dto.request;

import com.linkee.linkeeapi.quiz.command.application.dto.QuizInboundType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizWebsocketRequest {
        private QuizInboundType type;
        private Integer answerIndex;  // SUBMIT_ANSWER에서만 사용
        private Boolean ready;        // READY_TOGGLE에서만 사용
        private Integer roomCode;     // 비공개 시 비밀번호
    }