package com.linkee.linkeeapi.chat.command.instructure.repository;

import com.linkee.linkeeapi.chat.command.domain.aggregate.entity.Qna;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaQnaRepository extends JpaRepository<Qna, Integer> {
}
