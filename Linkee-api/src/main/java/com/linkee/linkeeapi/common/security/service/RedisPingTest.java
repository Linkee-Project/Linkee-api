package com.linkee.linkeeapi.common.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisPingTest implements CommandLineRunner {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            // PING 테스트
            String pong = redisTemplate.getConnectionFactory()
                    .getConnection()
                    .ping();
            System.out.println("Redis 연결 테스트 결과: " + pong);

            // 간단한 키-값 저장 테스트
            redisTemplate.opsForValue().set("TEST_KEY", "Hello Redis!");
            String value = redisTemplate.opsForValue().get("TEST_KEY");
            System.out.println("Redis에 저장된 값: " + value);
            System.out.println(redisTemplate.getConnectionFactory().getConnection());

        } catch (Exception e) {
            System.err.println("Redis 연결 실패: " + e.getMessage());
        }
    }
}
