package com.linkee.linkeeapi.quiz_room.command.infrastructure.scheduler;

import com.linkee.linkeeapi.common.enums.RoomStatus;
import com.linkee.linkeeapi.quiz_room.command.application.service.QuizRoomCommandService;
import com.linkee.linkeeapi.quiz_room.command.domain.aggregate.QuizRoom;
import com.linkee.linkeeapi.quiz_room.command.infrastructure.repository.QuizRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class QuizRoomScheduler {

    private final QuizRoomRepository quizRoomRepository;
    private final QuizRoomCommandService quizRoomCommandService;

    // 일정 주기로 비활성 퀴즈방 정리
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cleanupZombieRooms() {
        //  DB에서 직접 10분 이상 업데이트 안 된 방만 조회
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(10);
        List<QuizRoom> staleRooms = quizRoomRepository
                .findByRoomStatusAndUpdatedAtBefore(RoomStatus.P, cutoffTime);

        for (QuizRoom room : staleRooms) {
            quizRoomCommandService.forceEndRoom(room.getQuizRoomId());
            log.info("자동 종료된 방 ID: {}",  room.getQuizRoomId());
        }
    }
}
