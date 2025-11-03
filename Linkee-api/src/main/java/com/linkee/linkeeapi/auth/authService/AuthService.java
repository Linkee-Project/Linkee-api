package com.linkee.linkeeapi.auth.authService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthService {

    // 서비스단이나 다른곳에서 사용하기위해 Authentication 정보를 뽑아오는 서비스

    // 스프링 시큐리티 컨트롤러에선
    // @AuthenticationPrincipal UserDetails userDetails 이런식으로 받아오면 된다

    public String getCurrentUsername() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        return authentication.getName();
    }
}
