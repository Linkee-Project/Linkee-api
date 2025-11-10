package com.linkee.linkeeapi.quiz.query.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoomUserRankResponse {
    private Long roomMemberId;
    private int correctCount;
}
