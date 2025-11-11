package com.linkee.linkeeapi.users.command.application.service;

public interface UserCommandService {

    void updateNickname(Long userId ,String newNickName);

    void deleteUser(Long userId);
}
