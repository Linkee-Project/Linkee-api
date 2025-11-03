package com.linkee.linkeeapi.user.command.application.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequest {
    private String userEmail;
    private String userPassword;
    private String userNickname;
}
