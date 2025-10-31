package com.linkee.linkeeapi.user.controller;

import com.linkee.linkeeapi.common.ApiResponse;
import com.linkee.linkeeapi.user.model.dto.UserCreateRequest;
import com.linkee.linkeeapi.user.service.UserCommandService;
import com.linkee.linkeeapi.user_email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserCommandController {

    private final UserCommandService userCommandService;
    private final EmailService emailService; // 이메일 의존성 추가

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> register(
            @RequestBody UserCreateRequest request
    ) {
        try {
            userCommandService.registerUser(request);
            // 성공 시 201 응답
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success(null));
        } catch (IllegalStateException e) {
            // 서비스 로직에서 발생한 예외(예: 중복 닉네임)를 잡아서 400 Bad Request 응답으로 변환
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.failure("REGISTRATION_FAILED", e.getMessage()));
        }
//        userCommandService.registerUser(request);
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(ApiResponse.success(null));

    }

    // 이메일 인증 코드 발송 API
    @PostMapping("/send-verification")
    public ResponseEntity<ApiResponse<Void>> sendVerificationEmail(@RequestBody Map<String, String> payload) {
        try {
            String email = payload.get("email");
            emailService.sendVerificationEmail(email);

            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .success(true)
                    .message("인증 코드가 성공적으로 발송되었습니다.")
                    .timestamp(java.time.LocalDateTime.now())
                    .build();

            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(ApiResponse.failure("VERIFICATION_FAILED", e.getMessage()));
        }
    }

    // 이메일 인증 코드 확인 API
    @PostMapping("/verify-code")
    public ResponseEntity<ApiResponse<Boolean>> verifyCode(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String code = payload.get("code");
        boolean isVerified = emailService.verifyCode(email, code);

        if (isVerified) {
            return ResponseEntity.ok(ApiResponse.success(true));
        } else {
            return ResponseEntity.ok(ApiResponse.failure("VERIFICATION_FAILED", "인증 코드가 일치하지 않습니다."));
        }
    }
}
