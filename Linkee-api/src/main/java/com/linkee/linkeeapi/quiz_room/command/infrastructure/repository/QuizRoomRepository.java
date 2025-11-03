package com.linkee.linkeeapi.quiz_room.command.infrastructure.repository;

import com.linkee.linkeeapi.quiz_room.command.domain.aggregate.QuizRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRoomRepository extends JpaRepository<QuizRoom, Long> {
}
