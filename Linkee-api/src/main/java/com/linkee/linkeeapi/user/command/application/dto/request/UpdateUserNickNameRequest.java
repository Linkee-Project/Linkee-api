package com.linkee.linkeeapi.user.command.application.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserNickNameRequest {
    private Long userId;
    private String nickName;
}
