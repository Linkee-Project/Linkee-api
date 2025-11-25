package com.linkee.linkeeapi.users.command.application.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRoleRequest {
    private Long userId;
    private String newRole; // "ADMIN" or "USER"
}
