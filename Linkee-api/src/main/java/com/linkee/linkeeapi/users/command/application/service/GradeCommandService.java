package com.linkee.linkeeapi.users.command.application.service;

import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.users.command.application.dto.request.UpdateGradeNameRequest;
import com.linkee.linkeeapi.users.command.domain.entity.Grade;
import com.linkee.linkeeapi.users.command.infrastructure.repository.GradeRepository;
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
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REQUEST,"해당 등급이 존재하지 않습니다"));
        grade.modifyGradeName(request.getGradeName());
    }


    @Transactional
    public void deleteGrade(Long gradeId) {
        if (!gradeRepository.existsById(gradeId)) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST,"해당 등급이 존재하지 않습니다");
        }
        gradeRepository.deleteById(gradeId);
    }
}
