package com.linkee.linkeeapi.users.command.application.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserGradeCreateRequest {
    private Long userId;
    private Long gradeId;
    private Long categoryId;
}
