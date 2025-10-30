package com.linkee.linkeeapi.usergrade.model.entity;

import com.linkee.linkeeapi.category.model.entity.Category;
import com.linkee.linkeeapi.grade.model.entity.Grade;
import com.linkee.linkeeapi.user.model.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_user_grade")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_grade_id")
    private Long userGradeId;

    // 유저 (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 등급 (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id", nullable = false)
    private Grade grade;

    // 카테고리 (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // 승리 횟수
    @Column(name = "victory_count", nullable = false)
    private int victoryCount = 0;
}


