package com.linkee.linkeeapi.user_email_verification.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "verification_codes")
public class VerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false) // 회원가입 시 이메일 인증 여부에 대한 컬럼 추가
    private boolean verified = false;

    @Builder
    public VerificationCode(String email, String code, LocalDateTime expiryDate) {
        this.email = email;
        this.code = code;
        this.expiryDate = expiryDate;
        this.verified = false; // 생성 시 항상 false로 초기화
    }

    // 인증 코드 갱신을 위한 메소드
    public void updateCode(String code, LocalDateTime expiryDate) {
        this.code = code;
        this.expiryDate = expiryDate;
        this.verified = false; // 인증 코드 갱신 시 verified 상태를 다시 false로 리셋
    }

    // 인증 완료 상태로 변경하는 메소드
    public void setVerified() {
        this.verified = true; // 정상적으로 인증 완료 시 true
    }

}
