package com.linkee.linkeeapi.quiz_room.websocket.dto.response.data;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizUserResult {
    private Long userId;
    private String userName;
    private Long selectedOptionId;        // null이면 미제출
    private Boolean isCorrect;            // true/false
    private Integer responseTime;         // 한 문제 당 결과 15초 예정

}