package com.linkee.linkeeapi.users.command.infrastructure.repository;

import com.linkee.linkeeapi.users.command.domain.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade,Long> {
}
