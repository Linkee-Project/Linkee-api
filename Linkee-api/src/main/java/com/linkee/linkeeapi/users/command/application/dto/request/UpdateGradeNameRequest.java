package com.linkee.linkeeapi.users.command.application.dto.request;

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
