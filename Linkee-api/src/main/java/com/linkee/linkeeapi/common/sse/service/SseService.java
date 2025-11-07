package com.linkee.linkeeapi.common.sse.service;

import com.linkee.linkeeapi.common.sse.repository.SseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SseService {
    private static final Long DEFAULT_TIMEOUT = 60L*1000*60; // 1시간이 지나면 연결이 끊어짐
    private final SseRepository sseRepository;

    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        sseRepository.save(userId, emitter);

        // 연결이 끊어지거나 타임아웃되면 저장소에서 제거
        emitter.onCompletion(() -> sseRepository.deleteById(userId));
        emitter.onTimeout(() -> sseRepository.deleteById(userId));

        // 연결 직후, 더미 데이터 전송 (503 Service Unavailable 방지)
        sendToClient(emitter, userId, "EventStream Created. [userId=" + userId + "]");
        return emitter;
    }
    public void send(Long userId, String eventName, Object data) {
        SseEmitter emitter = sseRepository.findById(userId);
        if (emitter != null) {
            sendToClient(emitter, userId, data, eventName);
        }
    }

    private void sendToClient(SseEmitter emitter, Long userId, Object data) {
        sendToClient(emitter, userId, data, "sse");
    }

    private void sendToClient(SseEmitter emitter, Long userId, Object data, String eventName) {
        try {
            emitter.send(SseEmitter.event()
                    .id(userId.toString())
                    .name(eventName)
                    .data(data));
        } catch (IOException exception) {
            sseRepository.deleteById(userId);
        }
    }
}
