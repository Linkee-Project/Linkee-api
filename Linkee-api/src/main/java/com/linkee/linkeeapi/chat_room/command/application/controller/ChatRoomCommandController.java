package com.linkee.linkeeapi.chat_room.command.application.controller;

import com.linkee.linkeeapi.chat_room.command.application.dto.request.ChatRoomCreateRequestDto;
import com.linkee.linkeeapi.chat_room.command.application.dto.request.ChatRoomDeleteRequestDto;
import com.linkee.linkeeapi.chat_room.command.application.service.ChatRoomCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat-room")
public class ChatRoomCommandController {

    private final ChatRoomCommandService chatRoomCommandService;

    // 채팅방 / 게임방 생성
    @PostMapping
    public ResponseEntity<String> createRoom(@Valid @RequestBody ChatRoomCreateRequestDto request) {
        chatRoomCommandService.createRoom(request);
        return ResponseEntity.ok("방이 성공적으로 생성되었습니다.");
    }

    // 방장 게임방 나가기
    @PostMapping("/delete-game-room")
    public ResponseEntity<String> deleteGameRoom(@Valid @RequestBody ChatRoomDeleteRequestDto request) {
        chatRoomCommandService.deleteGameRoom(request);
        return ResponseEntity.ok("게임방이 성공적으로 종료되었습니다.");
    }

    // 채팅방 나가기
    @PostMapping("/leave-room")
    public ResponseEntity<String> leaveChatRoom(@Valid @RequestBody ChatRoomDeleteRequestDto request) {
        chatRoomCommandService.leaveRoom(request);
        return ResponseEntity.ok("방에서 나갔습니다.");
    }
}
