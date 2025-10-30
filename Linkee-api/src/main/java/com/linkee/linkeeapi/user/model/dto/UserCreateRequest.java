package com.linkee.linkeeapi.user.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor

/* 회원 가입 시 필요한 정보 기입 */
public class UserCreateRequest {
    private String userLoginId;
    private String userPw;
    private String userNickname;
}
