package com.linkee.linkeeapi.chat.command.application.service.command_service;

import com.linkee.linkeeapi.chat.chat_command.chat_domain.entity.ChatRoom;
import com.linkee.linkeeapi.chat.command.domain.dto.command_dto.request.ChatRoomCreateRequestDto;
import com.linkee.linkeeapi.chat.command.domain.dto.command_dto.request.ChatRoomDeleteRequestDto;
import com.linkee.linkeeapi.user.command.domain.entity.User;

public interface ChatRoomCommandService {

    //방 만들기
    ChatRoom createRoom(ChatRoomCreateRequestDto request);

    // 입장
    void enterRoom(Long roomId, User user);
    //방 삭제
    void deleteGameRoom(ChatRoomDeleteRequestDto request);

    void leaveRoom(ChatRoomDeleteRequestDto request);
}
