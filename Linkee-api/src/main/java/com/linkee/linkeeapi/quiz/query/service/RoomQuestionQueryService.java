package com.linkee.linkeeapi.quiz.query.service;

import com.linkee.linkeeapi.quiz.query.dto.response.RoomQuestionResponse;

import java.util.List;

public interface RoomQuestionQueryService {
    List<RoomQuestionResponse> getRoomQuestionsByRoomId(Long roomId);
}