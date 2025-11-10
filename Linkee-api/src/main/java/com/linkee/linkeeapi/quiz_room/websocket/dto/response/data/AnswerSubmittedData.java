package com.linkee.linkeeapi.quiz_room.websocket.dto.response.data;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerSubmittedData {
    private Long userId;
    private String userName;
    private Integer submittedCount;       // 3명 제출
    private Integer totalParticipants;    // 총 5명
}
