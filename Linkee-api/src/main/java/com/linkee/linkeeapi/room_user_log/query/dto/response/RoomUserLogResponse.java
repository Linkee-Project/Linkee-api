package com.linkee.linkeeapi.room_user_log.query.dto.response;

import com.linkee.linkeeapi.common.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class RoomUserLogResponse {
    private Long roomUserLogId;
    private Long roomQuestionId;
    private Long roomMemberId;
    private Status isCorrected;
}
