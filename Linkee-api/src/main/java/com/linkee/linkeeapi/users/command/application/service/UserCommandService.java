package com.linkee.linkeeapi.users.command.application.service;

import com.linkee.linkeeapi.users.command.application.dto.request.UpdateUserRoleRequest;

public interface UserCommandService {

    void updateNickname(Long userId ,String newNickName);

    void deleteUser(Long userId);

    void updateUserRole(UpdateUserRoleRequest request);
}
