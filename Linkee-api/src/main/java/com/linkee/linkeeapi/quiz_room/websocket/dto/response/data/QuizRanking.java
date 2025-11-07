package com.linkee.linkeeapi.quiz_room.websocket.dto.response.data;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizRanking {
    private Integer rank;
    private Long userId;
    private String userName;
    private Integer correctCount;
    private Integer wrongCount;
    private Boolean isMe;       //true면 해당 사용자 화면에 뜸
}
