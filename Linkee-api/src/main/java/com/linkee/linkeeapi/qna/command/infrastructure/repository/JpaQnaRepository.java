package com.linkee.linkeeapi.qna.command.infrastructure.repository;

import com.linkee.linkeeapi.qna.command.domain.aggregate.Qna;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaQnaRepository extends JpaRepository<Qna, Integer> {
}
