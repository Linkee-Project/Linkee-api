package com.linkee.linkeeapi.common.config.security;

import com.linkee.linkeeapi.auth.authService.CustomOAuth2UserService;
import com.linkee.linkeeapi.auth.handler.OAuth2SuccessHandler;
import com.linkee.linkeeapi.common.config.jwt.JwtFilter;
import com.linkee.linkeeapi.common.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter; // ✅ 기존 필터 유지
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // ✅ CORS 허용
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // ✅ JWT 구조이므로 세션 비활성화
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // ✅ 접근 권한 설정
                .authorizeHttpRequests(auth -> auth

                        // 1. 완전 공개 경로 먼저
                        .requestMatchers("/oauth2/**", "/error", "/accessDenied").permitAll()
                        .requestMatchers("/signup/**").permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/oauth/**").permitAll()

                        // swagger 공개
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        // WebSocket 공개
                        .requestMatchers(
                                "/quiz.html",
                                "/ws-stomp/**",
                                "/ws-chat/**",
                                "/chat/**",
                                "/notice/**",
                                "/ws/**",
                                "/sockjs/**"
                        ).permitAll()

                        .requestMatchers("/api/v1/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/v1/**").hasAnyAuthority("USER", "ADMIN")

                        .anyRequest().authenticated()
                )

///*                //폼로그인
//                .formLogin(form -> form
//                        .loginPage("/login/login.html")
//                        .loginProcessingUrl("/api/v1/auth/login") // 실제 로그인 처리 POST URL
//                        .defaultSuccessUrl("/notice/notice.html", true)
//                        .failureUrl("/login/login.html?error=true")
//                        .permitAll()
//                )*/

                .formLogin(form -> form.disable())


                // ✅ OAuth2 로그인 설정 (네이버용)
                .oauth2Login(oauth -> oauth
                        //.loginPage("/login/login.html")
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                        //.defaultSuccessUrl("/", true)
                        //.defaultSuccessUrl("/notice/notice.html", true)
                        .failureUrl("/accessDenied") // 추가 권장
                )




                // ✅ JWT 필터 추가
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    // 퀴즈방 웹소켓 테스트시 필요 설정
    // ✅ CORS 설정(127.0.0.1:5500, localhost:* ,http://localhost:5173(VueJS) 모두 허용 + Authorization 헤더 허용)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var c = new CorsConfiguration();
        // VSCode Live Server / 로컬 프론트들
        c.setAllowedOriginPatterns(java.util.List.of(
                "http://localhost:*",
                "http://127.0.0.1:*",
                "http://localhost:5173"
        ));
        c.setAllowedMethods(java.util.List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        c.setAllowedHeaders(java.util.List.of("Authorization","Content-Type","X-Requested-With"));
        c.setExposedHeaders(java.util.List.of("Authorization")); // 필요시
        c.setAllowCredentials(true);
        c.setMaxAge(3600L);

        var source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", c);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
