package com.linkee.linkeeapi.quiz.command.application.service;

import com.linkee.linkeeapi.quiz.command.application.dto.request.RoomUserLogCreateRequestDto;

public interface RoomUserLogCommandService {
    Long createRoomUserLog(RoomUserLogCreateRequestDto requestDto);
}
