package com.linkee.linkeeapi.room_question.query.service;

import com.linkee.linkeeapi.room_question.query.dto.response.RoomQuestionResponse;

import java.util.List;

public interface RoomQuestionQueryService {
    List<RoomQuestionResponse> getRoomQuestionsByRoomId(Long roomId);
}