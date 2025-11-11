package com.linkee.linkeeapi.quiz.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//  결과 조회 응답
public class ResultRowResponseDto {
    private Long userId;
    private String userNickname;
    private Integer correctCount;
    private Integer total;        // roomQuizLimit
}
