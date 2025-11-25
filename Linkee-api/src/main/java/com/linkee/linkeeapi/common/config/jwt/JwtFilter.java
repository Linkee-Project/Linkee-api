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

            // 유효하지 않은 AccessToken → 즉시 401 반환
            if (!jwtTokenProvider.validateToken(token)) {
                System.out.println("⚠️ [DEBUG] JwtFilter: 토큰 유효하지 않음 → 401 반환");

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return; // 🔥 더 이상 진행하지 않음
            }

            // -------- 토큰이 유효한 경우에만 아래 진행 --------
            String tokenType = jwtTokenProvider.getTokenType(token);
            if ("access".equals(tokenType)) {

                String username = jwtTokenProvider.getUsername(token);

                CustomUser customUser =
                        (CustomUser) customUserDetailsService.loadUserByUsername(username);

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
                System.out.println("⚠️ [DEBUG] JwtFilter: refreshToken 접근 시도");
            }
        }

        filterChain.doFilter(request, response);
    }

}
