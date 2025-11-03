package com.linkee.linkeeapi.user_grade.query.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserGradeSearchRequest {
    private Long userId;
    private Long categoryId;
    private Long gradeId;
    private String keyword;   // 유저 닉네임 등 검색용

    private Integer page;
    private Integer size;
    private Integer offset;
}
