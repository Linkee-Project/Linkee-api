package com.linkee.linkeeapi.quiz.command.application.controller;


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
import com.linkee.linkeeapi.user.command.application.service.util.UserFinder;
import com.linkee.linkeeapi.user.command.domain.entity.User;
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
    private final QuizRoomWebSocketService quizRoomSocketService;
    private final QuizRoomCommandService quizRoomCommandService;
    private final RoomMemberCommandService roomMemberCommandService;
    private final RoomMemberRepository roomMemberRepository;
    private final QuizRoomRepository quizRoomRepository;
    private final UserFinder userFinder;

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
                // 기존 startGame 로직 그대로 호출 (문제 배정/상태전환/인덱스 생성/스케줄러)
                quizRoomCommandService.startGame(roomId, userId);
            }

            case SUBMIT_ANSWER -> {
                if (message.getAnswerIndex() == null) {
                    throw new BusinessException(ErrorCode.INVALID_REQUEST, "answerIndex가 필요합니다.");
                }

                // ✅ 팀원이 만든 DTO로 변환
                QuizRoomSubmitAnswerRequestDto request = QuizRoomSubmitAnswerRequestDto.builder()
                        .quizRoomId(roomId)
                        .submittedOptionIndex(message.getAnswerIndex())
                        .build();

                // ✅ 팀원이 구현한 메서드 호출
                quizRoomCommandService.submitAnswer(request, userId);

                // ✅ WebSocket 브로드캐스트
                quizRoomSocketService.notifyAnswerSubmitted(request.getQuizRoomId(), userId);
            }

            case READY_TOGGLE -> {
                if (message.getReady() == null) {
                    throw new BusinessException(ErrorCode.INVALID_REQUEST, "ready 값이 필요합니다.");
                }
                QuizRoom quizRoom = quizRoomRepository.findById(roomId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.QUIZ_ROOM_NOT_FOUND));
                User user = userFinder.getById(userId);
                RoomMember roomMember = roomMemberRepository.findByQuizRoomAndMember(quizRoom, user)
                        .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_MEMBER_NOT_FOUND));

                // 준비 상태 토글(RoomMember 업데이트)
                roomMemberCommandService.toggleReady(roomMember.getRoomMemberId());

                // ✅ 멤버 목록 갱신 브로드캐스트
                quizRoomSocketService.broadcastMemberList(roomId);
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
