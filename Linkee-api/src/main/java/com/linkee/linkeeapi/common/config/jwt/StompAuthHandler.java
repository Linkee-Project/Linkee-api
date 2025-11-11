package com.linkee.linkeeapi.common.config.jwt;


import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.linkee.linkeeapi.common.model.CustomUser;
import com.linkee.linkeeapi.users.command.domain.entity.User;
import com.linkee.linkeeapi.users.command.infrastructure.repository.UserRepository;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StompAuthHandler implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // CONNECT 단계에서만 인증 수행
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = resolveToken(accessor);
            if (token == null || !jwtTokenProvider.validateToken(token)) {
                throw new IllegalArgumentException("❌ Invalid or missing JWT in STOMP CONNECT");
            }

            // JWT에서 이메일/역할 추출
            String userEmail = jwtTokenProvider.getUsername(token);
            String role = jwtTokenProvider.getRole(token);

            // DB 조회
            User user = userRepository.findByUserEmail(userEmail)
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + userEmail));

            // Security User 생성
            CustomUser customUser = new CustomUser(
                    user.getUserId(),
                    userEmail,
                    "",
                    "ROLE_" + role
            );

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(customUser, null, customUser.getAuthorities());
            accessor.setUser(authentication);

            System.out.println("✅ [STOMP CONNECT] userId=" + user.getUserId() +
                    ", email=" + userEmail + ", role=" + role);
        }

        return message;
    }

    // JWT 토큰 추출
    private String resolveToken(StompHeaderAccessor accessor) {
        List<String> headers = accessor.getNativeHeader("Authorization");
        if (headers != null && !headers.isEmpty()) {
            String token = headers.get(0);
            if (token.startsWith("Bearer ")) return token.substring(7);
            return token;
        }

        List<String> alt = accessor.getNativeHeader("X-ACCESS-TOKEN");
        if (alt != null && !alt.isEmpty()) return alt.get(0);

        return null;
    }
}