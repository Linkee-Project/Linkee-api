package com.linkee.linkeeapi.user.service;

import com.linkee.linkeeapi.user.model.dto.UserCreateRequest;
import com.linkee.linkeeapi.user.model.entity.User;
import com.linkee.linkeeapi.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserCommandServiceTest {

    @Autowired
    private UserCommandService userCommandService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("신규 사용자 회원가입")
    void creatUser() {
        //given
        UserCreateRequest request = new UserCreateRequest();
        request.setUserLoginId("test@example.com");
        request.setUserPw("password123");
        request.setUserNickname("testUser");

        //when
        userCommandService.registerUser(request);

        //then
        User foundUser = userRepository.findByUserLoginId("test@example.com").orElseThrow();
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUserLoginId()).isEqualTo("test@example.com");
        assertThat(foundUser.getUserNickname()).isEqualTo("testUser");
    }
}