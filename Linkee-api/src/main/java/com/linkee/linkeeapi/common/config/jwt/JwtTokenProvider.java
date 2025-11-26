package com.linkee.linkeeapi.common.config.jwt;

import com.linkee.linkeeapi.users.command.domain.entity.User;
import com.linkee.linkeeapi.users.command.infrastructure.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long accessTokenValidity;
    private final long refreshTokenValidity;

    private final UserRepository userRepository;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKeyBase64,
            @Value("${jwt.access-expiration}") long accessTokenValidity,
            @Value("${jwt.refresh-expiration}") long refreshTokenValidity, UserRepository userRepository) {
        this.userRepository = userRepository;

        byte[] decodedKey = Base64.getDecoder().decode(secretKeyBase64);
        this.key = Keys.hmacShaKeyFor(decodedKey);

        this.accessTokenValidity = accessTokenValidity;
        this.refreshTokenValidity = refreshTokenValidity;
    }

    // AccessToken
    public String createAccessToken(String userEmail, String role) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(userEmail)
                .claim("role", role)
                .claim("type", "access") // 🔹 추가
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidity))
                .signWith(key)
                .compact();
    }

    // RefreshToken
    public String createRefreshToken(String userEmail, String role) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(userEmail)
                .claim("role", role)
                .claim("type", "refresh") // 🔹 추가
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidity))
                .signWith(key)
                .compact();
    }

    public String getUsername(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            
        } catch (JwtException e) {
            return null;
        }
    }




    public String getTokenType(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("type", String.class);
        } catch (JwtException e) {
            return null;
        }
    }

    public String getRole(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("role", String.class);
        } catch (JwtException e) {
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }


    public User validateTokenAndGetUser(String token) {
        try {
            var claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();   // sub = email

            return userRepository.findByUserEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

        } catch (Exception e) {
            throw new RuntimeException("Invalid Token");
        }
    }


    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        String role = getRole(token);
        if (username == null) return null;

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
        return new UsernamePasswordAuthenticationToken(username, null, Collections.singleton(authority));
    }





}
