package com.linkee.linkeeapi.common.config.websocket;


import com.linkee.linkeeapi.common.config.jwt.StompAuthHandler;
import com.nimbusds.jwt.JWT;
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
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompAuthHandler stompAuthHandler;
    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 프론트에서 ws-chat 또는 ws-stomp 어느 쪽이든 연결 가능하게 허용
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*")
                .addInterceptors(jwtHandshakeInterceptor);


    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // STOMP CONNECT 시 JWT 검증 & Authentication 주입
        registration.interceptors(stompAuthHandler);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 클라이언트 → 서버 전송 경로 (Controller @MessageMapping)
        registry.setApplicationDestinationPrefixes("/app", "/pub");

        // 서버 → 클라이언트 송신 경로
        registry.enableSimpleBroker("/topic", "/sub", "/queue");

        // 1:1 메시징용 prefix
        registry.setUserDestinationPrefix("/user");
    }
}
