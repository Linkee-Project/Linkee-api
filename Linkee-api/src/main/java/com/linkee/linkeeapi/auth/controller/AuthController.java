package com.linkee.linkeeapi.auth.controller;

import com.linkee.linkeeapi.common.jwt.JwtTokenProvider;
import com.linkee.linkeeapi.common.security.service.RedisRefreshTokenService;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user.command.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisRefreshTokenService redisRefreshTokenService;

    private final long REFRESH_TOKEN_EXPIRE = 1000 * 60 * 60; // 1시간

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

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestParam String username) {
        User user = userRepository.findByUserEmail(username).orElse(null);

        if (user == null) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }

        return ResponseEntity.ok(Map.of(
                "userId", user.getUserId(),
                "userEmail", user.getUserEmail(),
                "userNickname", user.getUserNickname(),
                "userRole", user.getUserRole()
        ));
    }
}
