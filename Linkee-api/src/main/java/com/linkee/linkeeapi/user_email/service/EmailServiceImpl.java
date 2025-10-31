package com.linkee.linkeeapi.user_email.service;

import com.linkee.linkeeapi.user.repository.UserRepository;
import com.linkee.linkeeapi.user_email_verification.model.entity.VerificationCode;
import com.linkee.linkeeapi.user_email_verification.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender mailSender;
    private final VerificationCodeRepository verificationCodeRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void sendVerificationEmail(String to) {

        if (userRepository.findByUserLoginId(to).isPresent()) {
            throw new IllegalStateException("이미 가입된 이메일입니다.");
        }

        String verificationCode = createVerificationCode();
        String subject = "Linkee 회원가입 인증 코드입니다.";
        String text = "인증 코드: " + verificationCode;
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(5); // 5분 후 인증코드 만료

        // DB에 이메일이 이미 있는지 확인
        Optional<VerificationCode> existingCode = verificationCodeRepository.findByEmail(to);

        if (existingCode.isPresent()) {
            // 이미 있다면 코드와 만료 시간만 업데이트
            existingCode.get().updateCode(verificationCode, expiryDate);
        } else {
            // 없다면 새로 생성해서 저장
            VerificationCode newCode = VerificationCode.builder()
                    .email(to)
                    .code(verificationCode)
                    .expiryDate(expiryDate)
                    .build();
            verificationCodeRepository.save(newCode);
        }

        // 이메일 발송
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    @Override
    @Transactional
    public boolean verifyCode(String email, String code) {
        Optional<VerificationCode> storedCodeOpt = verificationCodeRepository.findByEmail(email);

        if (storedCodeOpt.isPresent()) {
            VerificationCode storedCode = storedCodeOpt.get();
            // 코드가 일치하고, 만료 시간이 지나지 않았다면
            if (storedCode.getCode().equals(code) && LocalDateTime.now().isBefore(storedCode.getExpiryDate())) {

                storedCode.setVerified();// / 인증 성공 시 DB에서 삭제하는 대신, '인증됨' 상태로 변경
//                verificationCodeRepository.save(storedCode); // @Transactional에 의해 자동 변경 감지 및 업데이트
//                verificationCodeRepository.delete(storedCode); // 인증 성공 시 DB에서 코드 삭제

                return true;
            }
        }
        return false;
    }

    private String createVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 6자리 랜덤 숫자 생성 -> 인증코드
        return String.valueOf(code);
    }
}
