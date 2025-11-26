package com.linkee.linkeeapi.users.command.application.dto.request;

import com.linkee.linkeeapi.common.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserStatusRequest {
    private Long userId;
    private Status status; // Y for active, N for inactive
}
