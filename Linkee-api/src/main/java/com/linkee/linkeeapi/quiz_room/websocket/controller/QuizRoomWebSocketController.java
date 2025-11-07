package com.linkee.linkeeapi.quiz_room.websocket.controller;


import com.linkee.linkeeapi.common.security.model.CustomUser;
import com.linkee.linkeeapi.quiz_room.websocket.dto.request.QuizWebSocketMessage;
import com.linkee.linkeeapi.quiz_room.websocket.service.QuizRoomSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class QuizRoomWebSocketController {
    private final QuizRoomSocketService quizRoomSocketService;

    @MessageMapping("/quiz-room/{roomId}")
    public void handleQuizMessage(
            @DestinationVariable Long roomId,
            @Payload QuizWebSocketMessage message,
            Principal principal
    ) {
        // StompHandler에서 주입해준 Principal에서 userId 추출
        CustomUser customUser = (CustomUser) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Long userId = customUser.getUserId();

        log.info("WS RECV: roomId={}, userId={}, type={}, payload={}", roomId, userId, message.getType(), message);

        // 메시지 타입에 따라 서비스 로직 호출
        switch (message.getType()) {
            case START_QUIZ:
                quizRoomSocketService.startQuiz(roomId, userId);
                break;

            case SUBMIT_ANSWER:
                quizRoomSocketService.submitAnswer(
                        roomId,
                        userId,
                        message.getQuestionId(),
                        message.getSelectedOptionId()
                );
                break;

            default:
                log.warn("Unhandled message type for roomId {}: {}", roomId, message.getType());
                quizRoomSocketService.sendErrorToUser(userId, "지원하지 않는 메시지 타입입니다: " + message.getType());
                break;
        }
    }


}
