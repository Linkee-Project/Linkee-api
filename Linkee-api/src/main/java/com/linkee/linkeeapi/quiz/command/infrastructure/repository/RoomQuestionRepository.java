package com.linkee.linkeeapi.quiz.command.infrastructure.repository;

import com.linkee.linkeeapi.quiz.command.domain.aggregate.QuizRoom;
import com.linkee.linkeeapi.quiz.command.domain.aggregate.RoomQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomQuestionRepository extends JpaRepository<RoomQuestion, Long> {
    Optional<RoomQuestion> findByQuizRoomAndQuizOrder(QuizRoom quizRoom, int quizOrder);
}
