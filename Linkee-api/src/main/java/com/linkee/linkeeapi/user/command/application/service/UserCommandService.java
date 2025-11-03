package com.linkee.linkeeapi.user.command.application.service;

import com.linkee.linkeeapi.user.command.application.dto.request.UpdateUserNickNameRequest;
import com.linkee.linkeeapi.user.command.application.dto.request.UserCreateRequest;

public interface UserCommandService {

    void createUser(UserCreateRequest request);

    void updateNickname(UpdateUserNickNameRequest request);

    void deleteUser(Long userId);
}
