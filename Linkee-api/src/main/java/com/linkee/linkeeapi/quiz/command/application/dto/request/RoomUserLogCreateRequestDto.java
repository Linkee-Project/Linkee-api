package com.linkee.linkeeapi.quiz.command.application.dto.request;

import com.linkee.linkeeapi.common.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoomUserLogCreateRequestDto {
    private Long roomQuestionId;
    private Long roomMemberId;
    private Status isCorrected;
}
