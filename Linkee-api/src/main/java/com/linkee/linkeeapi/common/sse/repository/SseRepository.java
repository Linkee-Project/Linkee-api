package com.linkee.linkeeapi.common.sse.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SseRepository {
    // Map 형태로 메모리에 저장 (SseEmitter)
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    // 새로운 사용자가 구독하면, 그 사용자의 ID와 연결 통로를 Map에 저장
    public void save(Long userId, SseEmitter emitter) {
        emitters.put(userId, emitter);
    }

    // 사용자가 접속을 끊으면, Map에서 해당 사용자의 연결 통로를 제거함
    public void deleteById(Long userId) {
        emitters.remove(userId);
    }

    // 특정 사용자에게 알림을 보내야 할 때, Map에서 그 사용자의 연결 통로를 찾아옴
    public SseEmitter findById(Long userId) {
        return emitters.get(userId);
    }

}
