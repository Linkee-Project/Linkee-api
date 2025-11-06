package com.linkee.linkeeapi.room_question.command.application.controller;

import com.linkee.linkeeapi.common.model.dto.ApiResponse;
import com.linkee.linkeeapi.room_question.command.application.dto.request.RoomQuestionCreateRequest;
import com.linkee.linkeeapi.room_question.command.application.service.RoomQuestionCommandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/room_questions")
@RequiredArgsConstructor
@Tag(name = "퀴즈", description = "퀴즈방 생성, 입장, 진행 관련 API")
public class RoomQuestionCommandController {

    private final RoomQuestionCommandService roomQuestionCommandService;

    @PostMapping
    public ApiResponse<Long> createRoomQuestion(@RequestBody RoomQuestionCreateRequest request) {
        Long roomQuestionId = roomQuestionCommandService.createRoomQuestion(request);
        return ApiResponse.success(roomQuestionId);
    }
}
