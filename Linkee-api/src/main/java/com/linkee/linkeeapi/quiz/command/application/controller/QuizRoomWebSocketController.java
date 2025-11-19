package com.linkee.linkeeapi.quiz.command.application.controller;


import com.linkee.linkeeapi.common.config.jwt.JwtTokenProvider;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.common.model.CustomUser;
import com.linkee.linkeeapi.quiz.command.application.dto.request.QuizRoomSubmitAnswerRequestDto;
import com.linkee.linkeeapi.quiz.command.application.service.QuizRoomCommandService;
import com.linkee.linkeeapi.quiz.command.domain.aggregate.QuizRoom;
import com.linkee.linkeeapi.quiz.command.infrastructure.repository.QuizRoomRepository;
import com.linkee.linkeeapi.quiz.command.application.dto.request.QuizWebsocketRequest;
import com.linkee.linkeeapi.quiz.command.application.service.QuizRoomWebSocketService;
import com.linkee.linkeeapi.quiz.command.application.service.RoomMemberCommandService;
import com.linkee.linkeeapi.quiz.command.domain.aggregate.RoomMember;
import com.linkee.linkeeapi.quiz.command.infrastructure.repository.RoomMemberRepository;

import com.linkee.linkeeapi.users.command.application.service.util.UserFinder;
import com.linkee.linkeeapi.users.command.domain.entity.User;
import com.linkee.linkeeapi.users.command.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import java.security.Principal;


@Slf4j
@Controller
@RequiredArgsConstructor
public class QuizRoomWebSocketController {

    private final QuizRoomWebSocketService quizRoomSocketService;
    private final QuizRoomCommandService quizRoomCommandService;
    private final RoomMemberCommandService roomMemberCommandService;
    private final RoomMemberRepository roomMemberRepository;
    private final QuizRoomRepository quizRoomRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @MessageMapping("/quiz/rooms/{roomId}")
    public void handleQuizMessage(@DestinationVariable Long roomId,
                                  @Payload QuizWebsocketRequest message,
                                  @Header(value = "Authorization", required = false) String authHeader,
                                  Principal principal) {

        Long userId = null;

        // 1) 먼저 principal 쪽에서 꺼냄
        if (principal instanceof UsernamePasswordAuthenticationToken token
                && token.getPrincipal() instanceof CustomUser customUser) {
            userId = customUser.getUserId();
        }

        // 2) principal 없으면 → 헤더에서 JWT 파싱 (fallback)
        if (userId == null) {
            if (authHeader == null || authHeader.isBlank()) {
                quizRoomSocketService.sendError(roomId, "UNAUTHORIZED_WS");
                log.warn("❌ WS 메시지에 Authorization 헤더 없음 (roomId={})", roomId);
                return;
            }

            String raw = authHeader.trim();
            String tokenStr = raw.startsWith("Bearer ") ? raw.substring(7) : raw;

            if (!jwtTokenProvider.validateToken(tokenStr)) {
                quizRoomSocketService.sendError(roomId, "UNAUTHORIZED_WS");
                log.warn("❌ WS 메시지 JWT 검증 실패 (roomId={})", roomId);
                return;
            }


            String email = jwtTokenProvider.getUsername(tokenStr);
            User user = userRepository.findByUserEmail(email)
                    .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_USER_ID));
            userId = user.getUserId();
        }

        log.info("WS RECV: roomId={}, userId={}, type={}, payload={}", roomId, userId, message.getType(), message);

        if (message.getType() == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "type이 필요합니다.");
        }

        switch (message.getType()) {
            case START_QUIZ -> {
                quizRoomCommandService.startGame(roomId, userId);
            }
            case SUBMIT_ANSWER -> {
                if (message.getAnswerIndex() == null) {
                    throw new BusinessException(ErrorCode.INVALID_REQUEST, "answerIndex가 필요합니다.");
                }
                QuizRoomSubmitAnswerRequestDto request = QuizRoomSubmitAnswerRequestDto.builder()
                        .quizRoomId(roomId)
                        .submittedOptionIndex(message.getAnswerIndex())
                        .build();
                quizRoomCommandService.submitAnswer(request, userId);
                quizRoomSocketService.notifyAnswerSubmitted(request.getQuizRoomId(), userId);
            }
            case READY_TOGGLE -> {
                QuizRoom quizRoom = quizRoomRepository.findById(roomId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.QUIZ_ROOM_NOT_FOUND));

                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_USER_ID));

                RoomMember roomMember = roomMemberRepository.findByQuizRoomAndMember(quizRoom, user)
                        .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_MEMBER_NOT_FOUND));

                roomMemberCommandService.toggleReady(roomMember.getRoomMemberId());
            }
            case JOIN -> log.info("JOIN received (noop) roomId={}, userId={}", roomId, userId);
            case LEAVE -> log.info("LEAVE received (noop) roomId={}, userId={}", roomId, userId);
        }
    }


    }
