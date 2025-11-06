package com.linkee.linkeeapi.chat.command.application.service.query_serivce;

import com.linkee.linkeeapi.chat.command.domain.dto.query_dto.request.ChatRoomListRequestDto;
import com.linkee.linkeeapi.chat.command.domain.dto.query_dto.request.GameRoomListRequestDto;
import com.linkee.linkeeapi.chat.command.domain.dto.query_dto.response.ChatRoomListResponseDto;
import com.linkee.linkeeapi.chat.command.domain.dto.query_dto.response.GameRoomListResponseDto;
import com.linkee.linkeeapi.chat.command.domain.mapper.ChatRoomMapper;
import com.linkee.linkeeapi.common.model.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomQueryServiceImpl implements ChatRoomQueryService{

    private final ChatRoomMapper chatRoomMapper;

    //내 채팅방 목록 조회
    @Override
    public PageResponse<ChatRoomListResponseDto> getMyChatRoomList(ChatRoomListRequestDto request) {
        int page = (request.getPage() != null) ? request.getPage() : 0;
        int size = (request.getSize() != null) ? request.getSize() : 10;
        int offset = page * size;

        List<ChatRoomListResponseDto> chatRooms = chatRoomMapper.findMyChatRooms(request.getUserId(),  offset, size);
        int total = chatRoomMapper.countMyChatRooms(request.getUserId());

        return PageResponse.from(chatRooms, offset, size, total);
    }

    //전체 게임방 목록 조회
    @Override
    public PageResponse<GameRoomListResponseDto> getGameRoomList(GameRoomListRequestDto request) {
        int page = (request.getPage() != null) ? request.getPage() : 0;
        int size = (request.getSize() != null) ? request.getSize() : 10;
        int offset = page * size;

        List<GameRoomListResponseDto> gameRooms = chatRoomMapper.findAllGameRooms(offset,size);
        int total = chatRoomMapper.countAllGameRooms();

        return PageResponse.from(gameRooms, offset, size, total);
    }
}
