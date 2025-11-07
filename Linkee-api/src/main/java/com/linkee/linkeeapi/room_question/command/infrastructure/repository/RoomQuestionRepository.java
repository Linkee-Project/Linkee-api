package com.linkee.linkeeapi.room_question.command.infrastructure.repository;

import com.linkee.linkeeapi.quiz_room.command.domain.aggregate.QuizRoom;
import com.linkee.linkeeapi.room_question.command.domain.aggregate.RoomQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomQuestionRepository extends JpaRepository<RoomQuestion, Long> {
    Optional<RoomQuestion> findByQuizRoomAndQuizOrder(QuizRoom quizRoom, int quizOrder);
}
