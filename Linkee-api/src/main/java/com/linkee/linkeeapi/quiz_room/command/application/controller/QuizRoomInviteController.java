package com.linkee.linkeeapi.quiz_room.command.application.controller;

import com.linkee.linkeeapi.common.model.dto.ApiResponse;
import com.linkee.linkeeapi.quiz_room.command.application.dto.request.QuizRoomInviteRequestDto;
import com.linkee.linkeeapi.quiz_room.command.application.service.QuizRoomInviteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/quiz-rooms") // 퀴즈방 관련 API 아래에 중첩
@RequiredArgsConstructor
@Tag(name = "퀴즈", description = "퀴즈방 생성, 입장, 진행, 초대 관련 API")
public class QuizRoomInviteController {

    private final QuizRoomInviteService quizRoomInviteService;

    /*
     * 퀴즈방 초대 요청을 처리합니다.
     * @param request 초대 요청 정보를 담은 DTO
     * @return 성공 응답
     */
    @PostMapping("/invite") // POST /api/v1/quiz-rooms/invite
    public ApiResponse<Void> inviteToQuizRoom(@RequestBody QuizRoomInviteRequestDto request) {
        quizRoomInviteService.sendInvite(request);
        return ApiResponse.success(null); // 성공 시 별도 데이터 없이 성공 응답
    }
}