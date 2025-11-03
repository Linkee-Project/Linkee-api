package com.linkee.linkeeapi.room_user_log.query.service;

import com.linkee.linkeeapi.room_user_log.query.dto.response.RoomUserLogResponse;
import com.linkee.linkeeapi.room_user_log.query.dto.response.RoomUserRankResponse;

import java.util.List;

public interface RoomUserLogQueryService {
    List<RoomUserLogResponse> getRoomUserLogs(Long roomMemberId);
    List<RoomUserRankResponse> getQuizResult(Long roomId);

}
