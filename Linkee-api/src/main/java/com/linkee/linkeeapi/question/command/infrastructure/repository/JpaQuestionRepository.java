package com.linkee.linkeeapi.question.command.infrastructure.repository;

import com.linkee.linkeeapi.question.command.domain.aggregate.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JpaQuestionRepository extends JpaRepository<Question,Long> {

    @Query("""
        select distinct q
        from Question q
        left join fetch q.options o
        left join fetch q.category c
        left join fetch q.user u
        where q.questionId = :id
          and q.isDeleted = com.linkee.linkeeapi.common.enums.Status.N
    """)
    Optional<Question> findByIdWithOptions(@Param("id") Long id);
}
