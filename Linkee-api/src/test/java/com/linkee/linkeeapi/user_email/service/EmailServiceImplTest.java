package com.linkee.linkeeapi.user_email.service;


import com.linkee.linkeeapi.user_email_verification.model.entity.VerificationCode;
import com.linkee.linkeeapi.user_email_verification.repository.VerificationCodeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
// MockBean의 빨간 줄은 버전 3.4.0 이상에서 지원 중단이라 생긴 이슈
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // 각 테스트가 끝난 후 DB 변경사항을 롤백하여 테스트 격리
class EmailServiceImplTest {

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @MockBean // 실제 이메일을 발송하는 대신 가짜(Mock) JavaMailSender를 주입
    private JavaMailSender javaMailSender;
    @Test
    @DisplayName("성공: 올바른 코드로 인증 시 true를 반환하고 DB의 코드는 '인증됨' 상태가 된다")
    void verifyCode_Success() {
        // given: DB에 인증 코드가 미리 저장되어 있을 때
        String email = "test@example.com";
        String code = "123456";
        VerificationCode savedCode = VerificationCode.builder()
                .email(email)
                .code(code)
                .expiryDate(LocalDateTime.now().plusMinutes(5))
                .build();
        verificationCodeRepository.save(savedCode);

        // when: 올바른 이메일과 코드로 인증을 시도하면
        boolean isVerified = emailService.verifyCode(email, code);

        // then:
        // 1. 결과는 true여야 한다.
        assertTrue(isVerified, "인증에 성공해야 합니다.");

        // 2. DB에서 해당 코드를 다시 조회했을 때, 'verified' 필드가 true여야 한다. (삭제되지 않아야 함)
        Optional<VerificationCode> verifiedCodeOpt = verificationCodeRepository.findByEmail(email);
        assertTrue(verifiedCodeOpt.isPresent(), "인증 성공 후에도 코드는 DB에 남아있어야 합니다.");
        assertTrue(verifiedCodeOpt.get().isVerified(), "인증된 코드의 verified 상태는 true여야 합니다.");
    }
}
