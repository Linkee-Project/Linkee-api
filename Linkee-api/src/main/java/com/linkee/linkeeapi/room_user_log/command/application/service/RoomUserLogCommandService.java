package com.linkee.linkeeapi.room_user_log.command.application.service;

import com.linkee.linkeeapi.room_user_log.command.application.dto.request.RoomUserLogCreateRequestDto;

public interface RoomUserLogCommandService {
    Long createRoomUserLog(RoomUserLogCreateRequestDto requestDto);
}
