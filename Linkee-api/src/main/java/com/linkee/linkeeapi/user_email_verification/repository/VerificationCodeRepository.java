package com.linkee.linkeeapi.user_email_verification.repository;

import com.linkee.linkeeapi.user_email_verification.model.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    // 이메일로 인증 코드를 찾기 위한 메소드
    Optional<VerificationCode> findByEmail(String email);
}
