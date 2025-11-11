package com.linkee.linkeeapi.quiz.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//  플레이 상태 응답
public class  PlayStateResponseDto {
    private Long roomId;
    private Integer currentIndex;
    private Integer totalMembers;
    private Integer readyCount;
    private Integer correctCount;
}
