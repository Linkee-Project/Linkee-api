package com.linkee.linkeeapi.question.command.infrastructure.repository;

import com.linkee.linkeeapi.question.command.domain.aggregate.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCommentRepository extends JpaRepository<Comment, Long> {

}
