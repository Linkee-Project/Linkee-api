package com.linkee.linkeeapi.quiz.command.application.controller;

import com.linkee.linkeeapi.common.model.CustomUser;
import com.linkee.linkeeapi.common.model.dto.ApiResponse;
import com.linkee.linkeeapi.quiz.command.application.dto.request.QuizRoomCreateRequestDto;
import com.linkee.linkeeapi.quiz.command.application.dto.request.QuizRoomSubmitAnswerRequestDto;
import com.linkee.linkeeapi.quiz.command.application.service.QuizRoomCommandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


/*
 * 퀴즈룸 생성 관련 HTTP 요청을 처리하는 컨트롤러.
 * 클라이언트의 요청을 받아 QuizRoomCommandService로 전달합니다.
 */
@Slf4j
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
    public ApiResponse<Long> createQuizRoom(@RequestBody QuizRoomCreateRequestDto request,
                                            @AuthenticationPrincipal CustomUser user) {

        log.info("✅ 퀴즈방 생성 요청: userId={}", user.getUserId());
        Long quizRoomId = quizRoomCommandService.create(request, user.getUserId());
        return ApiResponse.success(quizRoomId);
    }

    // 퀴즈방 나가기 (멤버)
    @PostMapping("/{quizRoomId}/leave")
    public ResponseEntity<String> leaveQuizRoom(@PathVariable Long quizRoomId,
                                                @AuthenticationPrincipal CustomUser user) {
        quizRoomCommandService.leaveQuizRoom(quizRoomId, user.getUserId());
        return ResponseEntity.ok("퀴즈방에서 나갔습니다.");
    }

    /* 퀴즈방 게임 시작
     * @param quizRoomId 게임을 시작할 퀴즈방의 ID
     * @return 성공 메시지 */
    @PostMapping("/{quizRoomId}/start")
    public ResponseEntity<String> startGame(@PathVariable Long quizRoomId,
                                            @AuthenticationPrincipal CustomUser user) {

        quizRoomCommandService.startGame(quizRoomId, user.getUserId());
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

    @PostMapping("/submit-answer")
    public ResponseEntity<String> submitAnswer(@RequestBody QuizRoomSubmitAnswerRequestDto request,
                                               @AuthenticationPrincipal CustomUser user) {

        quizRoomCommandService.submitAnswer(request, user.getUserId());
        return ResponseEntity.ok("답안이 제출되었습니다.");
    }
}
