package com.linkee.linkeeapi.quiz.websocket.config;

import com.linkee.linkeeapi.common.security.jwt.JwtTokenProvider;
import com.linkee.linkeeapi.common.security.model.CustomUser;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user.command.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // CONNECT 단계에서만 토큰 검증 및 인증 객체 주입
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String token = resolveToken(accessor);
            if (token == null || !jwtTokenProvider.validateToken(token)) {
                throw new IllegalArgumentException(" Invalid or missing JWT in STOMP CONNECT");
            }

            // 토큰에서 사용자 정보 추출
            String userEmail = jwtTokenProvider.getUsername(token);
            String role = jwtTokenProvider.getRole(token); // 예: USER / ADMIN

            // DB에서 userId 조회
            User user = userRepository.findByUserEmail(userEmail)
                    .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + userEmail));

            Long userId = user.getUserId();

            // CustomUser 객체 생성 (Spring Security User 상속)
            CustomUser customUser = new CustomUser(
                    userId,
                    userEmail,
                    "", // 비밀번호는 필요 없음 (WS 연결용)
                    "ROLE_" + role
            );

            // 인증 토큰 생성 및 주입 (Principal)
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(customUser, null, customUser.getAuthorities());

            accessor.setUser(authentication);

            System.out.println("✅ [STOMP CONNECT] userId=" + userId + ", email=" + userEmail + ", role=" + role);
        }

        return message;
    }

    // Authorization 헤더 또는 X-ACCESS-TOKEN 헤더에서 JWT 추출
    private String resolveToken(StompHeaderAccessor accessor) {
        List<String> authHeaders = accessor.getNativeHeader("Authorization");
        if (authHeaders != null && !authHeaders.isEmpty()) {
            String header = authHeaders.get(0);
            if (header.startsWith("Bearer ")) return header.substring(7);
            return header;
        }

        List<String> alt = accessor.getNativeHeader("X-ACCESS-TOKEN");
        if (alt != null && !alt.isEmpty()) return alt.get(0);

        return null;
    }
}