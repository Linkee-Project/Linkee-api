package com.linkee.linkeeapi.user_grade.command.infrastructure.repository;

import com.linkee.linkeeapi.category.command.aggregate.Category;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user_grade.command.domain.entity.UserGrade;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserGradeRepository extends JpaRepository<UserGrade, Long> {

    @Query("SELECT ug FROM UserGrade ug JOIN FETCH ug.grade WHERE ug.user.userId = :userId")
    List<UserGrade> findAllByUserId(@Param("userId") Long userId);

    Optional<UserGrade> findByUserAndCategory(User user, Category category);

}
