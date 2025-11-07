package com.linkee.linkeeapi.auth.mail;


import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.concurrent.TimeUnit;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final StringRedisTemplate redisTemplate;

    // application.yml에서 TTL 값 가져오기
    @Value("${auth.code.expiration-millis}")
    private long authCodeTTL;
    // 메일 from 유저 가져오기
    @Value("${spring.mail.username}")
    private String mailUsername;

    public EmailService(JavaMailSender mailSender, StringRedisTemplate redisTemplate) {
        this.mailSender = mailSender;
        this.redisTemplate = redisTemplate;
    }

    // 6자리 인증번호 생성
    private String generateAuthCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<6; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    // 로그인 인증번호 발송
    public void sendAuthEmail(String email) {

        if(email == null || email.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INCORRECT_FORMAT,"잘못된 이메일 주소입니다.");
        }


        String code = generateAuthCode();

        // Redis에 TTL 적용하여 저장 (키값 , 내용 , 만료시간 ,시간이 어떤단위인지명시)
        redisTemplate.opsForValue().set("EMAIL_AUTH:" + email, code, authCodeTTL, TimeUnit.MILLISECONDS);

        // 메일 발송
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email.trim());
        message.setFrom(mailUsername);  // 환경변수 기반
        message.setSubject("(Linkee) 이메일 인증번호 안내");
        message.setText("인증번호: [" + code +"]");
        String text = String.join("\n",
                "안녕하세요, Linkee입니다.",
                "",
                "요청하신 이메일 인증번호를 안내드립니다.",
                "인증번호: [" + code + "]",
                "",
                "정확히 입력하여 인증을 완료해주세요."
        );

        message.setText(text);

        mailSender.send(message);
    }

    // 로그인 인증번호 검증
    public boolean verifyCode(String email, String code) {
        String key = "EMAIL_AUTH:" + email;
        String savedCode = redisTemplate.opsForValue().get(key);

        if(savedCode != null && savedCode.equals(code)) {
            redisTemplate.delete(key); // 인증 성공 시 삭제
            return true;
        }
        return false;
    }



}