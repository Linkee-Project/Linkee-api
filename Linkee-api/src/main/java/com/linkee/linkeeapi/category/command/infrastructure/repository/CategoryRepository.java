package com.linkee.linkeeapi.category.command.infrastructure.repository;

import com.linkee.linkeeapi.category.command.aggregate.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
