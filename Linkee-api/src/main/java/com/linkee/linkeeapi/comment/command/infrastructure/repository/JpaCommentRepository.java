package com.linkee.linkeeapi.comment.command.infrastructure.repository;

import com.linkee.linkeeapi.comment.command.domain.aggregate.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCommentRepository extends JpaRepository<Comment, Long> {

}
