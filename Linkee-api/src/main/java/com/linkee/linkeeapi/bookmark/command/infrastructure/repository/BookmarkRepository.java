package com.linkee.linkeeapi.bookmark.command.infrastructure.repository;

import com.linkee.linkeeapi.bookmark.command.domain.aggregate.entity.Bookmark;
import com.linkee.linkeeapi.question.command.domain.aggregate.Question;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    void deleteByUserAndQuestion(User user, Question question);
    boolean existsByUserAndQuestion(User user, Question question);
}
