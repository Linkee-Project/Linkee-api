package com.linkee.linkeeapi.quiz_room.command.infrastructure.scheduler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class QuizRoomSchedulerConfig {

    @Bean
    public TaskScheduler quizRoomTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10); // 동시에 실행될 스케줄링 작업의 최대 개수
        scheduler.setThreadNamePrefix("quiz-room-scheduler-");
        scheduler.initialize();
        return scheduler;
    }
}
