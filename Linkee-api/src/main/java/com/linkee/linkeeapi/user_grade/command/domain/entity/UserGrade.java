package com.linkee.linkeeapi.user_grade.command.domain.entity;

import com.linkee.linkeeapi.category.command.aggregate.Category;
import com.linkee.linkeeapi.grade.command.domain.aggregate.entity.Grade;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_user_grade")
@Getter
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
    @Builder.Default
    @Column(name = "victory_count", nullable = false)
    private int victoryCount = 0;


    public void modifyVictoryCount(int victoryCount){
        this.victoryCount = victoryCount;
    }

    public void modifyGrade(Grade newGrade) {
        this.grade = newGrade;
    }
}


