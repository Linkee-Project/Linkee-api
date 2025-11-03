package com.linkee.linkeeapi.grade.command.infrastructure;

import com.linkee.linkeeapi.grade.command.domain.aggregate.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade,Long> {
}
