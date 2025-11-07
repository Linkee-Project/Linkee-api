package com.linkee.linkeeapi.quiz_current_index.command.infrastructure.repository;

import com.linkee.linkeeapi.quiz_current_index.command.domain.aggregate.QuizCurrentIndex;
import com.linkee.linkeeapi.quiz_room.command.domain.aggregate.QuizRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizCurrentIndexRepository extends JpaRepository<QuizCurrentIndex, Long> {

    Optional<QuizCurrentIndex> findByQuizRoom(QuizRoom quizRoom);
}
