package com.linkee.linkeeapi.auth.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkee.linkeeapi.auth.repository.AuthRedisRepository;
import com.linkee.linkeeapi.common.config.jwt.JwtTokenProvider;
import com.linkee.linkeeapi.common.service.CustomUserDetails;
import com.linkee.linkeeapi.common.service.RedisRefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider  jwtTokenProvider;
    private final RedisRefreshTokenService redisRefreshTokenService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    private static final long REFRESH_EXP = 1000 * 60 * 60;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        CustomUserDetails customUser = (CustomUserDetails) authentication.getPrincipal();

        String email = customUser.getEmail();
        String role = customUser.getUser().getUserRole().name();


        //JWT 발급 -- access & refresh
        String accessToken = jwtTokenProvider.createAccessToken(email, role);
        String refreshToken = jwtTokenProvider.createRefreshToken(email,role);

        //레디스 저장
        redisRefreshTokenService.save(email, refreshToken, REFRESH_EXP);

        //refresh토큰 쿠키로 전달
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)      // 개발환경: false, 배포시 https면 true
                .path("/")
                .maxAge(REFRESH_EXP / 1000)
                .sameSite("Lax")
                .build();

        //헤더에 쿠키를 담아 전달
        response.addHeader("Set-Cookie", refreshCookie.toString());

/*        //응답
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);*/


        // ✅ 로그인 성공 후 프론트로 리다이렉트
        //프론트(2567 포트 등)로 redirect 하고 싶다면 successHandler redirect URL만 변경하면 됨:
        /*
        * String redirectUrl = "http://localhost:5173/oauth/callback?token=" + accessToken;
          response.sendRedirect(redirectUrl);
        * */
        /*String redirectUrl = "http://localhost:5173/oauth/callback?token=" + accessToken + "&email=" + email;
        response.sendRedirect(redirectUrl);*/
        response.sendRedirect("http://localhost:5173/oauth/callback");

        /*response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        objectMapper.writeValue(response.getWriter(), tokens);*/

        log.info("✅ Access Token: {}", accessToken);
        log.info("✅ Refresh Token: {}", refreshToken);
    }
}
