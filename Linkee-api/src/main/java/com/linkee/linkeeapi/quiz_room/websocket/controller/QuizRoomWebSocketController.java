package com.linkee.linkeeapi.quiz_room.websocket.controller;


import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.common.security.model.CustomUser;
import com.linkee.linkeeapi.quiz_room.websocket.dto.request.QuizWebsocketRequest;
import com.linkee.linkeeapi.quiz_room.websocket.service.QuizRoomWebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import java.security.Principal;

import static com.linkee.linkeeapi.quiz_room.websocket.dto.QuizInboundType.READY_TOGGLE;
import static com.linkee.linkeeapi.quiz_room.websocket.dto.QuizMessageType.SUBMIT_ANSWER;

@Slf4j
@Controller
@RequiredArgsConstructor
public class QuizRoomWebSocketController {
    private final QuizRoomWebSocketService quizRoomSocketService;

    @MessageMapping("/quiz-room/{roomId}")
    public void handleQuizMessage(
            @DestinationVariable Long roomId,
            @Payload QuizWebsocketRequest message,
            Principal principal
    ) {
        // StompHandler에서 주입해준 Principal에서 userId 추출
        CustomUser customUser = (CustomUser) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Long userId = customUser.getUserId();

        log.info("WS RECV: roomId={}, userId={}, type={}, payload={}", roomId, userId, message.getType(), message);

        if (message.getType() == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "type이 필요합니다.");
        }

        // 메시지 타입에 따라 서비스 로직 호출
        switch (message.getType()) {
            case START_QUIZ -> {
                // ✅ 기존 startGame 로직 그대로 호출 (문제 배정/상태전환/인덱스 생성/스케줄러)
                quizRoomCommandService.startGame(roomId, userId);
            }

            case SUBMIT_ANSWER -> {
                if (message.getAnswerIndex() == null) {
                    throw new BusinessException(ErrorCode.INVALID_REQUEST, "answerIndex가 필요합니다.");
                }
                // ✅ 선택값은 저장하지 않고, 정답 여부만 RoomUserLog에 저장 (정책 반영)
                quizRoomCommandService.submitAnswer(roomId, userId, message.getAnswerIndex());
            }

            case READY_TOGGLE -> {
                // (옵션) 준비 토글을 WS로 받을 경우 — REST 없이도 동작
                if (message.getReady() == null) {
                    throw new BusinessException(ErrorCode.INVALID_REQUEST, "ready 값이 필요합니다.");
                }
                quizRoomCommandService.toggleReady(roomId, userId, message.getReady());
                // 토글 결과는 필요 시 socket으로 멤버 목록/상태 갱신 브로드캐스트
            }

            case JOIN -> {
                // (옵션) 클라에서 “방 입장” 이벤트를 WS로 보낼 때 사용
                // 일반적으로는 REST에서 멤버 조인 처리 / 여기서는 단순 알림만 가능
                // 필요하면 RoomMember를 생성하고 ready=N으로 초기화하는 로직을 Command에 추가
                log.info("JOIN received (noop) roomId={}, userId={}", roomId, userId);
            }

            case LEAVE -> {
                // (옵션) 방 퇴장 이벤트 — 현재는 REST의 leaveQuizRoom 사용 권장
                log.info("LEAVE received (noop) roomId={}, userId={}", roomId, userId);
            }
        }


    }
}
