package com.linkee.linkeeapi.quiz_room.query.dto.response;

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
    private Integer correctCount;
    private Integer total;
    private Double accuracy;
}
