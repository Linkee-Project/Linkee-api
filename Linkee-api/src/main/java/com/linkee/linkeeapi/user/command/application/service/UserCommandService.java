package com.linkee.linkeeapi.user.command.application.service;

import com.linkee.linkeeapi.user.command.application.dto.request.UpdateUserNickNameRequest;
import com.linkee.linkeeapi.user.command.application.dto.request.UserCreateRequest;

public interface UserCommandService {

    void updateNickname(Long userId ,String newNickName);

    void deleteUser(Long userId);
}
