package com.linkee.linkeeapi.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkee.linkeeapi.auth.repository.AuthRedisRepository;
import com.linkee.linkeeapi.common.security.jwt.JwtTokenProvider;
import com.linkee.linkeeapi.common.security.service.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider  jwtTokenProvider;
    private final AuthRedisRepository authRedisRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        CustomUserDetails customUser = (CustomUserDetails) authentication.getPrincipal();

        String email = customUser.getEmail();
        String role = customUser.getUser().getUserRole().name();

        //JWT 발급
        String accessToken = jwtTokenProvider.createAccessToken(email, role);
        String refreshToken = jwtTokenProvider.createRefreshToken(email);

        //응답
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        authRedisRepository.save(email, refreshToken);

        // ✅ 로그인 성공 후 프론트로 리다이렉트
        String redirectUrl = "http://localhost:8080/?token=" + accessToken + "&email=" + email;

        response.sendRedirect(redirectUrl);

        /*response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        objectMapper.writeValue(response.getWriter(), tokens);*/
    }
}
