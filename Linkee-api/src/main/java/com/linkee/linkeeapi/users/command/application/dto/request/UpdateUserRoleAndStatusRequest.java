package com.linkee.linkeeapi.users.command.application.dto.request;

import com.linkee.linkeeapi.common.enums.Status;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRoleAndStatusRequest {
    private Long userId;
    private String newRole; // "ADMIN", "USER", or null if not updating role
    private Status status;  // Y, N, or null if not updating status
}
