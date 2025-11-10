package com.linkee.linkeeapi.quiz_room.websocket.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class StompConfig implements WebSocketMessageBrokerConfigurer {
    private final StompHandler stompHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // SockJS
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*")
                .setAllowedOrigins("http://localhost:5500", "http://127.0.0.1:5500")
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // STOMP CONNECT에서 JWT 검증 & Principal 주입
        registration.interceptors(stompHandler);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 클라이언트 → 서버
        registry.setApplicationDestinationPrefixes("/pub");
        // 서버 → 클라이언트 (브로드캐스트/개인큐)
        registry.enableSimpleBroker("/sub", "/queue");
        registry.setUserDestinationPrefix("/user"); //개인
    }


}
