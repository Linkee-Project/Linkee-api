package com.linkee.linkeeapi.quiz_room.query.controller;

import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.common.model.dto.ApiResponse;
import com.linkee.linkeeapi.quiz_room.query.dto.response.PlayStateResponseDto;
import com.linkee.linkeeapi.quiz_room.query.dto.response.QuizRoomListResponseDto;
import com.linkee.linkeeapi.quiz_room.query.dto.response.QuizRoomResponseDto;
import com.linkee.linkeeapi.quiz_room.query.dto.response.ResultRowResponseDto;
import com.linkee.linkeeapi.quiz_room.query.service.QuizRoomQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quiz-rooms")
@RequiredArgsConstructor
@Tag(name = "퀴즈", description = "퀴즈방 생성, 입장, 진행 관련 API")
public class QuizRoomQueryController {

    private final QuizRoomQueryService quizRoomService;

    // 방 목록 조회 (페이징)
    @GetMapping
    public ApiResponse<PageResponse<QuizRoomListResponseDto>> getRooms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<QuizRoomListResponseDto> rooms = quizRoomService.findAllRooms(page, size);
        return ApiResponse.success(rooms);
    }

    // 빠른 시작 (null 처리 추가)
    @GetMapping("/quick-start")
    public ResponseEntity<ApiResponse<QuizRoomResponseDto>> quickStart() {
        QuizRoomResponseDto availableRoom = quizRoomService.findAvailableRoom();

        if (availableRoom == null) {
            // 참여 가능한 방이 없으면 404 Not Found 응답
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.failure("ROOM_NOT_FOUND", "참여 가능한 방이 없습니다."));
        }

        // 방이 있으면 200 OK 와 함께 데이터 응답
        return ResponseEntity.ok(ApiResponse.success(availableRoom));
    }

    // 플레이 상태 조회
    @GetMapping("/{roomId}/state")
    public ResponseEntity<ApiResponse<PlayStateResponseDto>> getPlayState(@PathVariable Long roomId) {
        PlayStateResponseDto playState = quizRoomService.getPlayState(roomId);

        if (playState == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.failure("ROOM_NOT_FOUND", "해당 방을 찾을 수 없습니다."));
        }

        return ResponseEntity.ok(ApiResponse.success(playState));
    }

    // 결과 조회
    @GetMapping("/{roomId}/results")
    public ApiResponse<List<ResultRowResponseDto>> getResults(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<ResultRowResponseDto> results = quizRoomService.getResultsByRoom(roomId, page, size);
        return ApiResponse.success(results);
    }
}
