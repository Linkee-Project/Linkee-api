package com.linkee.linkeeapi.common.security.config;

import com.linkee.linkeeapi.common.security.jwt.JwtFilter;
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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

//                        // requestMatchers 사용시 저 경로 빼고는 인증때문에 막히니 개발할땐 닫아놓았다
//                        // 공개 엔드포인트
//                        .requestMatchers("/api/v1/auth/login", "/api/v1/auth/refresh", "/api/v1/public/**").permitAll()
//                        // 민감한 엔드포인트(토큰 필요)
//                        .requestMatchers("/api/v1/users/me").hasRole("USER")         // 예: 사용자 전용
//                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")       // 예: 관리자 전용
//                        // 나머지는 인증 필요
//                        .anyRequest().authenticated()
                                .anyRequest().permitAll()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
