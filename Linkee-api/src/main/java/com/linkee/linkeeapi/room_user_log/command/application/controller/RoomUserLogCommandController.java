package com.linkee.linkeeapi.room_user_log.command.application.controller;

import com.linkee.linkeeapi.room_user_log.command.application.dto.request.RoomUserLogCreateRequestDto;
import com.linkee.linkeeapi.room_user_log.command.application.service.RoomUserLogCommandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/* 퀴즈방 내 사용자의 문제 풀이 결과(정답 여부 등)를 기록 */
@RestController
@RequestMapping("/api/v1/room_user_logs")
@RequiredArgsConstructor
@Tag(name = "퀴즈", description = "퀴즈방 생성, 입장, 진행 관련 API")
public class RoomUserLogCommandController {

    private final RoomUserLogCommandService roomUserLogCommandService;

    @PostMapping
    public ResponseEntity<Long> createRoomUserLog(@RequestBody RoomUserLogCreateRequestDto requestDto) {
        Long roomUserLogId = roomUserLogCommandService.createRoomUserLog(requestDto);
        return new ResponseEntity<>(roomUserLogId, HttpStatus.CREATED);
    }
}
