package com.linkee.linkeeapi.chat_room.command.application.service;

import com.linkee.linkeeapi.chat_room.command.application.dto.request.ChatRoomCreateRequestDto;
import com.linkee.linkeeapi.chat_room.command.application.dto.request.ChatRoomDeleteRequestDto;

public interface ChatRoomCommandService {

    //방 만들기
    void createRoom(ChatRoomCreateRequestDto request);

    //방 삭제
    void deleteGameRoom(ChatRoomDeleteRequestDto request);

    void leaveRoom(ChatRoomDeleteRequestDto request);
}
