package com.linkee.linkeeapi.grade.command.application.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGradeNameRequest {

    private Long GradeId;
    private String gradeName;

}
