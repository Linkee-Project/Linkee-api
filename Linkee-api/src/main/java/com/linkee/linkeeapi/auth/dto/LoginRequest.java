package com.linkee.linkeeapi.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String userEmail;
    private String password;
}
