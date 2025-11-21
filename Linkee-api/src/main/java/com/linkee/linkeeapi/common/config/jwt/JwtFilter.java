package com.linkee.linkeeapi.common.config.jwt;

import com.linkee.linkeeapi.common.model.CustomUser;
import com.linkee.linkeeapi.common.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // 토큰 유효성 검증
            if (jwtTokenProvider.validateToken(token)) {
                // 토큰 타입 확인 (accessToken만 SecurityContext 세팅)
                String tokenType = jwtTokenProvider.getTokenType(token);
                if ("access".equals(tokenType)) {

                    String username = jwtTokenProvider.getUsername(token);

                    // DB에서 CustomUserDetails 로드
                    CustomUser customUser = (CustomUser) customUserDetailsService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    customUser,
                                    null,
                                    customUser.getAuthorities()
                            );

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    System.out.println("✅ [DEBUG] JwtFilter 인증 성공, user: " + username);
                } else {
                    // refreshToken이면 인증 안 함
                    System.out.println("⚠️ [DEBUG] JwtFilter: refreshToken으로 접근 시도, 인증 불가");
                }
            } else {
                System.out.println("⚠️ [DEBUG] JwtFilter: 토큰 유효하지 않음");
            }
        }

        filterChain.doFilter(request, response);
    }
}
