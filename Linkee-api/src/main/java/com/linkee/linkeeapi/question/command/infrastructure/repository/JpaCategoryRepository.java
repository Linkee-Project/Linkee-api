package com.linkee.linkeeapi.question.command.infrastructure.repository;

import com.linkee.linkeeapi.question.command.domain.aggregate.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCategoryRepository extends JpaRepository<Category, Long> {
}
