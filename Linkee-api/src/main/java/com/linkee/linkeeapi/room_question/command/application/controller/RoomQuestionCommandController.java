package com.linkee.linkeeapi.room_question.command.application.controller;

import com.linkee.linkeeapi.common.model.dto.ApiResponse;
import com.linkee.linkeeapi.room_question.command.application.dto.request.RoomQuestionCreateRequest;
import com.linkee.linkeeapi.room_question.command.application.service.RoomQuestionCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/room_questions")
@RequiredArgsConstructor
public class RoomQuestionCommandController {

    private final RoomQuestionCommandService roomQuestionCommandService;

    @PostMapping
    public ApiResponse<Long> createRoomQuestion(@RequestBody RoomQuestionCreateRequest request) {
        Long roomQuestionId = roomQuestionCommandService.createRoomQuestion(request);
        return ApiResponse.success(roomQuestionId);
    }
}
