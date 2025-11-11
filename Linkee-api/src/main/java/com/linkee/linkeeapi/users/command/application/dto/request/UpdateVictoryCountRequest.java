package com.linkee.linkeeapi.users.command.application.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateVictoryCountRequest {
    private Long userGradeId;
    private int victoryCount;
}
