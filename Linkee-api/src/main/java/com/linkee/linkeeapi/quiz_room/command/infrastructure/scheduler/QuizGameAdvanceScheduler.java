package com.linkee.linkeeapi.quiz_room.command.infrastructure.scheduler;

import com.linkee.linkeeapi.common.enums.RoomStatus;
import com.linkee.linkeeapi.quiz_current_index.command.domain.aggregate.QuizCurrentIndex;
import com.linkee.linkeeapi.quiz_current_index.command.infrastructure.repository.QuizCurrentIndexRepository;
import com.linkee.linkeeapi.quiz_room.command.application.service.QuizRoomCommandService;
import com.linkee.linkeeapi.quiz_room.command.domain.aggregate.QuizRoom;
import com.linkee.linkeeapi.quiz_room.command.infrastructure.repository.QuizRoomRepository;
import com.linkee.linkeeapi.quiz_room.websocket.service.QuizRoomWebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
public class QuizGameAdvanceScheduler {

    private final TaskScheduler quizRoomTaskScheduler;
    private final QuizRoomCommandService quizRoomCommandService;
    private final QuizRoomWebSocketService quizRoomWebSocketService;
    private final QuizRoomRepository quizRoomRepository;
    private final QuizCurrentIndexRepository quizCurrentIndexRepository;

    public QuizGameAdvanceScheduler(
            TaskScheduler quizRoomTaskScheduler,
            @Lazy QuizRoomCommandService quizRoomCommandService,
            QuizRoomWebSocketService quizRoomWebSocketService,
            QuizRoomRepository quizRoomRepository,
            QuizCurrentIndexRepository quizCurrentIndexRepository
    ) {
        this.quizRoomTaskScheduler = quizRoomTaskScheduler;
        this.quizRoomCommandService = quizRoomCommandService;
        this.quizRoomWebSocketService = quizRoomWebSocketService;
        this.quizRoomRepository = quizRoomRepository;
        this.quizCurrentIndexRepository = quizCurrentIndexRepository;
    }

    @Async
    public void scheduleAdvanceQuestion(Long quizRoomId, long delayMillis) {
        quizRoomTaskScheduler.schedule(() -> {
            try {
                // 1. 현재 문제 번호 조회
                QuizRoom room = quizRoomRepository.findById(quizRoomId).orElseThrow();
                QuizCurrentIndex index = quizCurrentIndexRepository.findByQuizRoom(room).orElseThrow();

                // 2. 문제 결과 브로드캐스트
                quizRoomWebSocketService.broadcastQuestionResult(quizRoomId, index.getCurrentQuizIndex());

                // 3. 15초 대기 (결과 표시)
                Thread.sleep(15000);

                // 4. 다음 문제로 진행
                quizRoomCommandService.advanceNextQuestion(quizRoomId);

                // 5. 게임 종료 시 최종 순위 브로드캐스트
                QuizRoom updatedRoom = quizRoomRepository.findById(quizRoomId).orElseThrow();
                if (updatedRoom.getRoomStatus() == RoomStatus.E) {
                    quizRoomWebSocketService.broadcastQuizFinished(quizRoomId);
                }

            } catch (Exception e) {
                log.error("문제 진행 실패: roomId={}", quizRoomId, e);
            }
        }, Instant.now().plusMillis(delayMillis));
    }
}
