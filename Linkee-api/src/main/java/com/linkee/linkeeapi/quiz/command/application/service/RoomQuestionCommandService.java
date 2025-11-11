package com.linkee.linkeeapi.quiz.command.application.service;

import com.linkee.linkeeapi.quiz.command.application.dto.request.RoomQuestionCreateRequest;

public interface RoomQuestionCommandService {
    Long createRoomQuestion(RoomQuestionCreateRequest roomQuestionCreateRequest);
}
