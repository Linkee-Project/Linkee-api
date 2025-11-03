package com.linkee.linkeeapi.grade.query.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeResponse {

    private Long gradeId;
    private String gradeName;
}
