package com.linkee.linkeeapi.quiz_room.websocket.dto.response.data;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizRankingData {
        private Integer totalQuestions;  // UI에 "총 10문제" 표시용
        private List<QuizRanking> rankings;
}
