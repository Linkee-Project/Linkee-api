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

    @MessageMapping("/quiz-room/{roomId}")
    public void handleQuizMessage(@DestinationVariable Long roomId,
                                  @Payload QuizWebsocketRequest message,
                                  Principal principal) {
        var auth = (UsernamePasswordAuthenticationToken) principal;
        var customUser = (CustomUser) auth.getPrincipal();
        Long userId = customUser.getUserId();

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
                quizRoomSocketService.broadcastMemberList(roomId);
            }
            case JOIN -> log.info("JOIN received (noop) roomId={}, userId={}", roomId, userId);
            case LEAVE -> log.info("LEAVE received (noop) roomId={}, userId={}", roomId, userId);
        }
    }


    }
