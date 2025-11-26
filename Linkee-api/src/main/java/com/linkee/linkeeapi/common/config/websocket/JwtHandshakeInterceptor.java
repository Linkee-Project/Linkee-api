package com.linkee.linkeeapi.common.config.websocket;


import com.linkee.linkeeapi.common.config.jwt.JwtTokenProvider;
import com.linkee.linkeeapi.users.command.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

@Configuration
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtHandshakeInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler handler,
                                   Map<String, Object> attributes) {

        String token = extractToken(request);
        if (token == null) return false;

        User user = jwtTokenProvider.validateTokenAndGetUser(token);
        attributes.put("user", user);
        attributes.put("token", token);

        return true;
    }


    private String extractToken(ServerHttpRequest request) {

        // 1) URL QueryString 에서 token 읽기 (브라우저용)
        String query = request.getURI().getQuery();
        if (query != null && query.startsWith("token=")) {
            return query.substring(6);
        }

        // 2) Header 에서 Authorization 읽기 (서버-서버 연결용)
        HttpHeaders headers = request.getHeaders();
        String authHeader = headers.getFirst("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

}
