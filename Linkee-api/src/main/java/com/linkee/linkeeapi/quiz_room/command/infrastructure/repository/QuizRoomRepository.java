package com.linkee.linkeeapi.quiz_room.command.infrastructure.repository;

import com.linkee.linkeeapi.common.enums.RoomStatus;
import com.linkee.linkeeapi.quiz_room.command.domain.aggregate.QuizRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface QuizRoomRepository extends JpaRepository<QuizRoom, Long> {

    List<QuizRoom> findByRoomStatusAndUpdatedAtBefore(RoomStatus roomStatus, LocalDateTime updateAt);
}
