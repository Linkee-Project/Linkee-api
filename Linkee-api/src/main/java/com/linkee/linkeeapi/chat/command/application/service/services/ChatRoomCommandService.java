package com.linkee.linkeeapi.chat.command.application.service.services;

import com.linkee.linkeeapi.chat.command.domain.aggregate.entity.ChatRoom;
import com.linkee.linkeeapi.chat.command.application.dto.request.ChatRoomCreateRequestDto;
import com.linkee.linkeeapi.chat.command.application.dto.request.ChatRoomDeleteRequestDto;
import com.linkee.linkeeapi.users.command.domain.entity.User;

public interface ChatRoomCommandService {

    //방 만들기
    ChatRoom createRoom(ChatRoomCreateRequestDto request);

    // 입장
    void enterRoom(Long roomId, User user);
    //방 삭제
    void deleteGameRoom(ChatRoomDeleteRequestDto request);

    void leaveRoom(ChatRoomDeleteRequestDto request);
}
