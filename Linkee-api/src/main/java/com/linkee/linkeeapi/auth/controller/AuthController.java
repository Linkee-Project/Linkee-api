package com.linkee.linkeeapi.auth.controller;

import com.linkee.linkeeapi.auth.authService.UserAuthService;
import com.linkee.linkeeapi.auth.mail.EmailRequest;
import com.linkee.linkeeapi.auth.mail.EmailService;
import com.linkee.linkeeapi.auth.mail.EmailVerifyRequest;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.common.security.jwt.JwtTokenProvider;
import com.linkee.linkeeapi.common.security.service.RedisRefreshTokenService;
import com.linkee.linkeeapi.user.command.application.dto.request.UserCreateRequest;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user.command.infrastructure.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "회원", description = "회원가입, 로그인, 계정, 프로필 관련 API")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisRefreshTokenService redisRefreshTokenService;
    private final UserAuthService userAuthService;
    private final EmailService emailService;

    private final long REFRESH_TOKEN_EXPIRE = 1000 * 60 * 60; // 1시간

    @PostMapping("/signup")
    public ResponseEntity<String> createUser(@RequestBody UserCreateRequest request) {

        if (userRepository.findByUserEmail(request.getUserEmail()).isPresent()) {
            // 이미 존재하는 이메일일 때 처리
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        if (!emailService.verifyCode(request.getUserEmail(), request.getUserEmail())) {
            throw new IllegalArgumentException("이메일 인증이 필요합니다.");
        }

        userAuthService.createUser(request);

        return ResponseEntity.ok("회원가입 완료!");
    }




    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String userEmail,
                                   @RequestParam String password) {

        User user = userRepository.findByUserEmail(userEmail).orElse(null);

        if (user == null || !passwordEncoder.matches(password, user.getUserPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        String role = user.getUserRole().name();
        String accessToken = jwtTokenProvider.createAccessToken(userEmail, role);
        String refreshToken = jwtTokenProvider.createRefreshToken(userEmail);

        redisRefreshTokenService.save(userEmail, refreshToken, REFRESH_TOKEN_EXPIRE);



        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        ));
    }


    // 비밀번호 변경을 위한 이메일 인증코드전송
    @PostMapping("/password/reset/request")
    public ResponseEntity<String> requestPasswordReset(@RequestBody EmailRequest emailRequest) {

        userRepository.findByUserEmail(emailRequest.getEmail())
                .orElseThrow( ()-> new IllegalArgumentException("유저 정보가 없습니다"));

        emailService.sendAuthEmail(emailRequest.getEmail());

        return ResponseEntity.ok("인증 코드가 발송되었습니다.");
    }

    // 인증 코드검증
    @PostMapping("/password/reset/verify")
    public String verifyAuth(@RequestBody @Valid EmailVerifyRequest req) {
        boolean result = emailService.verifyCode(req.getEmail(), req.getCode());
        return result ? "인증 성공!" : "인증 실패 또는 만료됨";
    }

    // 새로운 비밀번호로 변경
    @PostMapping("/password/reset")
    public ResponseEntity<String> resetPassword(@RequestParam String email,
                                                @RequestParam String newPassword) {

        userAuthService.resetToTemporaryPassword(email,newPassword);

        return ResponseEntity.ok("비밀번호가 변경되었습니다.");
    }





    @DeleteMapping("/logout")
    public ResponseEntity<String> logout() {
        // jwt 토큰은 따로 서버에서 관리하는게아니다
        // 클라이언트에서 jwt 쿠키에서 직접 삭제 하거나
        // 만료시간을 짧게 잡아서 자연스럽게 토큰값이 사라지는 역할
        return ResponseEntity.ok("로그아웃 완료!");
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestParam String userEmail,
                                     @RequestParam String refreshToken) {

        if (!redisRefreshTokenService.isValid(userEmail, refreshToken)
                || !jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid refresh token"));
        }

        String role = jwtTokenProvider.getRole(refreshToken);
        String newAccessToken = jwtTokenProvider.createAccessToken(userEmail, role);

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }



}
