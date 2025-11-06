package com.linkee.linkeeapi.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final OAuth2AuthorizedClientService authorizedClientService;

    // 현재 로그인한 사용자의 Access Token 확인
    @GetMapping("/token")
    public String getAccessToken(OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName()
        );

        if (client != null && client.getAccessToken() != null) {
            return client.getAccessToken().getTokenValue(); // Access Token 반환
        } else {
            return "Access Token 없음";
        }
    }
}
