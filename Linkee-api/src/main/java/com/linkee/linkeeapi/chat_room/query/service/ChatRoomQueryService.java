package com.linkee.linkeeapi.chat_room.query.service;

import com.linkee.linkeeapi.chat_room.query.dto.request.ChatRoomListRequestDto;
import com.linkee.linkeeapi.chat_room.query.dto.request.GameRoomListRequestDto;
import com.linkee.linkeeapi.chat_room.query.dto.response.ChatRoomListResponseDto;
import com.linkee.linkeeapi.chat_room.query.dto.response.GameRoomListResponseDto;
import com.linkee.linkeeapi.common.model.PageResponse;

public interface ChatRoomQueryService {

    //자신의 채팅방 목록 조회
    PageResponse<ChatRoomListResponseDto> getMyChatRoomList(ChatRoomListRequestDto request);

    //게임방 목록 전체 조회
    PageResponse<GameRoomListResponseDto> getGameRoomList(GameRoomListRequestDto request);
}
