package com.linkee.linkeeapi.user_grade.command.application.service;


import com.linkee.linkeeapi.category.command.aggregate.Category;
import com.linkee.linkeeapi.category.command.infrastructure.repository.CategoryRepository;
import com.linkee.linkeeapi.grade.command.domain.aggregate.entity.Grade;
import com.linkee.linkeeapi.grade.command.infrastructure.GradeRepository;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user.command.infrastructure.repository.UserRepository;
import com.linkee.linkeeapi.user_grade.command.application.dto.request.UpdateVictoryCountRequest;
import com.linkee.linkeeapi.user_grade.command.application.dto.request.UserGradeCreateRequest;
import com.linkee.linkeeapi.user_grade.command.domain.entity.UserGrade;
import com.linkee.linkeeapi.user_grade.command.infrastructure.repository.UserGradeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserGradeCommandService {

    private final UserGradeRepository userGradeRepository;
    private final UserRepository userRepository;
    private final GradeRepository gradeRepository;
    private final CategoryRepository categoryRepository;


    public UserGrade createUserGrade(UserGradeCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        Grade grade = gradeRepository.findById(request.getGradeId())
                .orElseThrow(() -> new IllegalArgumentException("해당 등급이 존재하지 않습니다."));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다."));

        UserGrade userGrade = UserGrade.builder()
                .user(user)
                .grade(grade)
                .category(category)
                .victoryCount(0)
                .build();

        return userGradeRepository.save(userGrade);
    }

    @Transactional
    public void updateVictoryCount(UpdateVictoryCountRequest request) {
        UserGrade userGrade = userGradeRepository.findById(request.getUserGradeId())
                .orElseThrow(() -> new IllegalArgumentException("해당 UserGrade가 존재하지 않습니다."));

        userGrade.modifyVictoryCount(request.getVictoryCount());
    }


    @Transactional
    public void deleteUserGrade(Long userGradeId) {
        if (!userGradeRepository.existsById(userGradeId)) {
            throw new IllegalArgumentException("해당 UserGrade가 존재하지 않습니다.");
        }
        userGradeRepository.deleteById(userGradeId);
    }


}
