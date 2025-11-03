package com.linkee.linkeeapi.grade.command.application.service;

import com.linkee.linkeeapi.grade.command.application.dto.request.UpdateGradeNameRequest;
import com.linkee.linkeeapi.grade.command.domain.aggregate.entity.Grade;
import com.linkee.linkeeapi.grade.command.infrastructure.GradeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GradeCommandService {


    private final GradeRepository gradeRepository;


    @Transactional
    public Grade createGrade(String gradeName) {
        Grade grade = Grade.builder()
                .gradeName(gradeName)
                .build();
        return gradeRepository.save(grade);
    }


    @Transactional
    public void updateGradeName(UpdateGradeNameRequest request) {
        Grade grade = gradeRepository.findById(request.getGradeId())
                .orElseThrow(() -> new IllegalArgumentException("해당 등급이 존재하지 않습니다."));
        grade.modifyGradeName(request.getGradeName());
    }


    @Transactional
    public void deleteGrade(Long gradeId) {
        if (!gradeRepository.existsById(gradeId)) {
            throw new IllegalArgumentException("해당 등급이 존재하지 않습니다. id=" + gradeId);
        }
        gradeRepository.deleteById(gradeId);
    }
}
