package com.linkee.linkeeapi.room_question.command.application.service;

import com.linkee.linkeeapi.room_question.command.application.dto.request.RoomQuestionCreateRequest;

public interface RoomQuestionCommandService {
    Long createRoomQuestion(RoomQuestionCreateRequest roomQuestionCreateRequest);
}
