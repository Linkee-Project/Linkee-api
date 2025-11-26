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
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler handler,
            Map<String, Object> attributes
    ) {
        String token = extractToken(request);

        if (token == null) return false;

        User user = jwtTokenProvider.validateTokenAndGetUser(token);

        // WebSocket 세션에 저장
        attributes.put("user", user);

        return true;
    }


    private String extractToken(ServerHttpRequest request) {
        // 1) QueryString 전체에서 token= 값을 찾아내기
        String query = request.getURI().getQuery();
        if (query != null) {
            for (String param : query.split("&")) {
                if (param.startsWith("token=")) {
                    return param.substring("token=".length());
                }
            }
        }

        // 2) Header 에서 Authorization 읽기 (WebSocket native)
        HttpHeaders headers = request.getHeaders();
        List<String> authHeader = headers.get("Authorization");

        if (authHeader != null && !authHeader.isEmpty()) {
            String bearer = authHeader.get(0);
            if (bearer.startsWith("Bearer ")) {
                return bearer.substring(7);
            }
        }

        return null;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

}
