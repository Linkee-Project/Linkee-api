package com.linkee.linkeeapi.grade.command.domain.aggregate.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_grade")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grade_id")
    private Long gradeId;

    @Builder.Default
    @Column(name = "grade_name", length = 5, nullable = false)
    private String gradeName = "B"; // 기본값: 브론즈

    public void modifyGradeName(String newName) {
        this.gradeName = newName;
    }

}
