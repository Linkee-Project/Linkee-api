package com.linkee.linkeeapi.quiz.query.service;

import com.linkee.linkeeapi.quiz.query.dto.response.RoomUserLogResponse;
import com.linkee.linkeeapi.quiz.query.dto.response.RoomUserRankResponse;

import java.util.List;

public interface RoomUserLogQueryService {
    List<RoomUserLogResponse> getRoomUserLogs(Long roomMemberId);
    List<RoomUserRankResponse> getQuizResult(Long roomId);

}
