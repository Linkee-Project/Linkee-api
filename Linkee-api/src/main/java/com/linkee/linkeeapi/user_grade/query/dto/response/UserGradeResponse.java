package com.linkee.linkeeapi.user_grade.query.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserGradeResponse {
    private Long userGradeId;
    private Long userId;
    private String userNickname;
    private Long gradeId;
    private String gradeName;
    private Long categoryId;
    private String categoryName;
    private int victoryCount;
}
