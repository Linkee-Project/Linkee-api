package com.linkee.linkeeapi.question.command.infrastructure.repository;

import com.linkee.linkeeapi.question.command.domain.aggregate.Bookmark;
import com.linkee.linkeeapi.question.command.domain.aggregate.Question;
import com.linkee.linkeeapi.users.command.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBookmarkRepository extends JpaRepository<Bookmark, Long> {

    void deleteByUserAndQuestion(User user, Question question);
    boolean existsByUserAndQuestion(User user, Question question);
}
