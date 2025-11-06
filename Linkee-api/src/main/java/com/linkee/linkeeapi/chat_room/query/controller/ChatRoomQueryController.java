package com.linkee.linkeeapi.chat_room.query.controller;

import com.linkee.linkeeapi.chat_room.query.dto.request.ChatRoomListRequestDto;
import com.linkee.linkeeapi.chat_room.query.dto.request.GameRoomListRequestDto;
import com.linkee.linkeeapi.chat_room.query.dto.response.ChatRoomListResponseDto;
import com.linkee.linkeeapi.chat_room.query.dto.response.GameRoomListResponseDto;
import com.linkee.linkeeapi.chat_room.query.service.ChatRoomQueryService;
import com.linkee.linkeeapi.common.model.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat-room")
@Tag(name = "자율", description = "자율방(자유 출제방) 관련 API")
@Tag(name = "커뮤니케이션", description = "친구 및 채팅 기능 관련 API")
public class ChatRoomQueryController {
    private final ChatRoomQueryService chatRoomQueryService;

    //자신의 채팅방 목록 조회
    @PostMapping("/chat-list")
    public ResponseEntity<PageResponse<ChatRoomListResponseDto>> getUserChatRooms(
            @Valid @RequestBody ChatRoomListRequestDto request) {

        return ResponseEntity.ok(chatRoomQueryService.getMyChatRoomList(request));
    }

    @PostMapping("/game-list")
    public ResponseEntity<PageResponse<GameRoomListResponseDto>> getAllGameRooms(
            @Valid @RequestBody GameRoomListRequestDto request) {

        return ResponseEntity.ok(chatRoomQueryService.getGameRoomList(request));
    }

}
