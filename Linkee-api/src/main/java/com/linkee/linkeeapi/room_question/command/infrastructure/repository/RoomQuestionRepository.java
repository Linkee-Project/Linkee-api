package com.linkee.linkeeapi.room_question.command.infrastructure.repository;

import com.linkee.linkeeapi.room_question.command.domain.aggregate.RoomQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomQuestionRepository extends JpaRepository<RoomQuestion, Long> {
}
