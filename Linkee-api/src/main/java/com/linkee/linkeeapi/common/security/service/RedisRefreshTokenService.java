package com.linkee.linkeeapi.common.security.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisRefreshTokenService {

    // Spring Data Redis에서 제공하는 Redis와의 주요 통신 객체
    private final StringRedisTemplate redisTemplate;

    @Transactional
    public void save(String userEmail, String refreshToken, long expireTimeMillis) {
        try {
            System.out.println("저장한다");
            redisTemplate.opsForValue().set(
                    "refresh:" + userEmail,
                    refreshToken,
                    Duration.ofMillis(expireTimeMillis)
            );
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


        System.out.println(redisTemplate.opsForValue().get("refresh:" + userEmail));



    }

    public boolean isValid(String userEmail, String refreshToken) {
        String stored = redisTemplate.opsForValue().get("refresh:" + userEmail);
        return stored != null && stored.equals(refreshToken);
    }

    public void delete(String userEmail) {
        redisTemplate.delete("refresh:" + userEmail);
    }
}
