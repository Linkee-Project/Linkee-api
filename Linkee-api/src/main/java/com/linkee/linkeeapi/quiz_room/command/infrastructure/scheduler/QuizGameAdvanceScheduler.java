package com.linkee.linkeeapi.quiz_room.command.infrastructure.scheduler;

import com.linkee.linkeeapi.quiz_room.command.application.service.QuizRoomCommandService;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class QuizGameAdvanceScheduler {

    private final TaskScheduler quizRoomTaskScheduler;
    private final QuizRoomCommandService quizRoomCommandService;

    public QuizGameAdvanceScheduler(TaskScheduler quizRoomTaskScheduler, @Lazy QuizRoomCommandService quizRoomCommandService) {
        this.quizRoomTaskScheduler = quizRoomTaskScheduler;
        this.quizRoomCommandService = quizRoomCommandService;
    }

    @Async // 이 메소드 자체는 비동기로 실행되어 스케줄러 스레드를 블록하지 않음
    public void scheduleAdvanceQuestion(Long quizRoomId, long delayMillis) {
        // 딜레이 후 advanceNextQuestion을 호출하는 태스크를 스케줄링
        quizRoomTaskScheduler.schedule(() -> {
            // 실제 advanceNextQuestion 로직은 QuizRoomCommandService에서 처리
            // 이 부분은 스케줄러 스레드에서 실행되지만, 내부적으로 @Async가 적용된
            // advanceNextQuestion을 호출하면 해당 메소드는 별도의 스레드에서 실행됨
            quizRoomCommandService.advanceNextQuestion(quizRoomId);
        }, Instant.now().plusMillis(delayMillis));
    }
}
