package com.linkee.linkeeapi.user.service;

import com.linkee.linkeeapi.user.model.dto.UserCreateRequest;
import com.linkee.linkeeapi.user.model.entity.User;
import com.linkee.linkeeapi.user.repository.UserRepository;
import com.linkee.linkeeapi.user_email_verification.model.entity.VerificationCode;
import com.linkee.linkeeapi.user_email_verification.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final VerificationCodeRepository verificationCodeRepository;

    @Transactional
    public void registerUser(UserCreateRequest request) {
        String email = request.getUserLoginId();
        String nickname = request.getUserNickname();

        VerificationCode verification = verificationCodeRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("이메일 인증 절차를 시작하지 않았습니다."));

        if (!verification.isVerified()) {
            throw new IllegalStateException("이메일 인증이 완료되지 않았습니다.");
        }

        if (userRepository.existsByUserNickname(nickname)) {
            throw new IllegalStateException("이미 사용 중인 닉네임입니다.");
        }

        //중복 회원 체크 로직 등 로직 필요
        User user = modelMapper.map(request,User.class);
        user.setEncodedPassword(passwordEncoder.encode(request.getUserPw()));
        userRepository.save(user);

        verificationCodeRepository.delete(verification);
    }
}
