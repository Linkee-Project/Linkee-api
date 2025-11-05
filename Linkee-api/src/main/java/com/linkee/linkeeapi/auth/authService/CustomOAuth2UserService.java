package com.linkee.linkeeapi.auth.authService;

import com.linkee.linkeeapi.common.enums.Role;
import com.linkee.linkeeapi.common.security.service.CustomUserDetails;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user.command.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String ,Object> attributes = oAuth2User.getAttributes();

        String email;
        String name;
        String naverId;

        if (registrationId.equals("naver")) {
            Map<String, Object> response  = (Map<String, Object>) attributes.get("response");
            email = (String) response.get("email");
            name = (String) response.get("name");
            naverId = (String) response.get("id");
        }else  {
            throw new OAuth2AuthenticationException("지원하지 않는 로그인 방식 입니다.");
        }

        User user = userRepository.findByUserEmail(email)
                .orElseGet(() ->  userRepository.save(User.createSocialUser(naverId, email, name, Role.USER)));

        return new CustomUserDetails(user, attributes);
    }
}
