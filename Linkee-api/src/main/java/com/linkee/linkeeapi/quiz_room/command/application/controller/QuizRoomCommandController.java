package com.linkee.linkeeapi.quiz_room.command.application.controller;

import com.linkee.linkeeapi.common.model.dto.ApiResponse;
import com.linkee.linkeeapi.quiz_room.command.application.dto.request.QuizRoomCreateRequestDto;
import com.linkee.linkeeapi.quiz_room.command.application.dto.request.QuizRoomDeleteRequestDto;
import com.linkee.linkeeapi.quiz_room.command.application.service.QuizRoomCommandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/*
 * 퀴즈룸 생성 관련 HTTP 요청을 처리하는 컨트롤러.
 * 클라이언트의 요청을 받아 QuizRoomCommandService로 전달합니다.
 */
@RestController
@RequestMapping("/api/v1/quiz-rooms")
@RequiredArgsConstructor
@Tag(name = "퀴즈", description = "퀴즈방 생성, 입장, 진행 관련 API")
public class QuizRoomCommandController {

    private final QuizRoomCommandService quizRoomCommandService;

    /*
     * 새로운 퀴즈룸을 생성합니다.
     * @param request 퀴즈룸 생성에 필요한 데이터를 담은 DTO
     * @return 생성된 퀴즈룸의 ID를 포함하는 API 응답
     */
    @PostMapping
    public ApiResponse<Long> createQuizRoom(@RequestBody QuizRoomCreateRequestDto request) {
        // 추후 Spring Security가 적용되면 @AuthenticationPrincipal 에서 유저 정보를 받아와야 합니다.
        Long userId = 1L; // 임시로 방장(유저) ID를 1로 설정합니다.

        Long quizRoomId = quizRoomCommandService.create(request, userId);
        return ApiResponse.success(quizRoomId);
    }

    // 퀴즈방 나가기 (멤버)
    @PostMapping("/leave-quiz-room")
    public ResponseEntity<String> leaveQuizRoom(@Valid @RequestBody QuizRoomDeleteRequestDto request) {
        quizRoomCommandService.leaveQuizRoom(request);
        return ResponseEntity.ok("퀴즈방에서 나갔습니다.");
    }

    /* 퀴즈방 게임 시작
     * @param quizRoomId 게임을 시작할 퀴즈방의 ID
     * @return 성공 메시지 */
    @PostMapping("/{quizRoomId}/start")
    public ResponseEntity<String> startGame(@PathVariable Long quizRoomId) {
        //  요청을 보낸 사용자가 방장인지 확인하는 로직 추가 필요
        Long userId = 1L;
        quizRoomCommandService.startGame(quizRoomId,  userId);
        return ResponseEntity.ok("게임이 시작되었습니다.");
    }

    /* 현재 퀴즈방을 다음 문제로 진행시킵니다.
     * @param quizRoomId 진행할 퀴즈방의 ID
     * @return 성공 메시지 */
    @PostMapping("/{quizRoomId}/next")
    public ResponseEntity<String> advanceNextQuestion(@PathVariable Long quizRoomId) {
        quizRoomCommandService.advanceNextQuestion(quizRoomId);
        return ResponseEntity.ok("다음 문제로 진행되었습니다.");
    }
}
