package com.linkee.linkeeapi.common.security.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisRefreshTokenService {

    private final StringRedisTemplate redisTemplate;

    public void save(String userEmail, String refreshToken, long expireTimeMillis) {
        redisTemplate.opsForValue().set(
                "refresh:" + userEmail,
                refreshToken,
                Duration.ofMillis(expireTimeMillis)
        );
    }

    public boolean isValid(String userEmail, String refreshToken) {
        String stored = redisTemplate.opsForValue().get("refresh:" + userEmail);
        return stored != null && stored.equals(refreshToken);
    }

    public void delete(String userEmail) {
        redisTemplate.delete("refresh:" + userEmail);
    }
}
