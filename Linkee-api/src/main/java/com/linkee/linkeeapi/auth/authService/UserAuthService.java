package com.linkee.linkeeapi.auth.authService;

import com.linkee.linkeeapi.auth.mail.EmailService;
import com.linkee.linkeeapi.category.command.aggregate.Category;
import com.linkee.linkeeapi.category.command.infrastructure.repository.CategoryRepository;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.grade.command.domain.aggregate.entity.Grade;
import com.linkee.linkeeapi.grade.command.infrastructure.GradeRepository;
import com.linkee.linkeeapi.user.command.application.dto.request.UserCreateRequest;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user.command.infrastructure.repository.UserRepository;
import com.linkee.linkeeapi.user_grade.command.domain.entity.UserGrade;
import com.linkee.linkeeapi.user_grade.command.infrastructure.repository.UserGradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final GradeRepository gradeRepository;
    private final UserGradeRepository userGradeRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;


    public void createUser(UserCreateRequest request) {


        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getUserPassword());
        //유저 저장
        User user = User.builder()
                .userEmail(request.getUserEmail())
                .userPassword(encodedPassword)
                .userNickname(request.getUserNickname())
                .build();
        userRepository.save(user);


        // 유저 정상 가입되면 카테고리별로 기본 등급 저장 (유저등급 테이블)
        List<Category> categories = categoryRepository.findAll();
        //기본 등급
        Grade defaultGrade = gradeRepository.findById(1L)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REQUEST,"등급정보가 존재하지 않습니다."));

        //각 카테고리별 UserGrade 생성
        for (Category category : categories) {
            UserGrade userGrade = UserGrade.builder()
                    .user(user)
                    .grade(defaultGrade)
                    .category(category)
                    .victoryCount(0)
                    .build();
            userGradeRepository.save(userGrade);
        }

    }


    @Transactional
    public void resetToTemporaryPassword(String email ,String newPassword) {
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_USER_ID));

        user.changePassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);

    }




}
