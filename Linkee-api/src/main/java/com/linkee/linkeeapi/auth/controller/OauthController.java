package com.linkee.linkeeapi.auth.controller;

import com.linkee.linkeeapi.auth.repository.AuthRedisRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Tag(name = "회원", description = "회원가입, 로그인, 계정, 프로필 관련 API")
public class OauthController {

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final AuthRedisRepository authRedisRepository;

    private static final String OAUTH2_AUTHORIZATION_BASE_URI = "/oauth2/authorization";

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/user")
    public String user() {
        return "user";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        Map<String, String> oauth2AuthenticationUrls = new HashMap<>();

        if (clientRegistrationRepository instanceof Iterable<?>) {
            Iterable<ClientRegistration> clientRegistrations =
                    (Iterable<ClientRegistration>) clientRegistrationRepository;

            System.out.println("✅ [DEBUG] 등록된 OAuth2 클라이언트 목록:");
            clientRegistrations.forEach(registration -> {
                System.out.println(" - " + registration.getClientName() + " (" + registration.getRegistrationId() + ")");
                oauth2AuthenticationUrls.put(
                        registration.getClientName(),
                        OAUTH2_AUTHORIZATION_BASE_URI + "/" + registration.getRegistrationId()
                );
            });
        } else {
            System.out.println("❌ [DEBUG] clientRegistrationRepository 가 Iterable 이 아닙니다!");
        }

        model.addAttribute("oauth2AuthenticationUrls", oauth2AuthenticationUrls);
        return "login";
    }


    // ✅ Redis에 저장된 RefreshToken 삭제 (소셜로그인 로그아웃)
    @PostMapping("/logout") public ResponseEntity<String> logout(@RequestParam String email) {
        authRedisRepository.delete(email); // Redis에서 RefreshToken 삭제
        return ResponseEntity.ok("로그아웃 완료");
    }

    @RequestMapping("/accessDenied")
    public String accessDenied() {
        return "accessDenied";
    }
}
