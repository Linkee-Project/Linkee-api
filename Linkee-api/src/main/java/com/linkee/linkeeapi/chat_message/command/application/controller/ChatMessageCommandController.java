package com.linkee.linkeeapi.chat_message.command.application.controller;

import com.linkee.linkeeapi.chat_message.command.application.dto.request.ChatMessageCreateRequest;
import com.linkee.linkeeapi.chat_message.command.application.dto.request.MarkAsReadRequest;
import com.linkee.linkeeapi.chat_message.command.application.service.ChatMessageCommandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat-messages")
@Tag(name = "자율", description = "자율방(자유 출제방) 관련 API")
@Tag(name = "커뮤니케이션", description = "친구 및 채팅 기능 관련 API")
public class ChatMessageCommandController {

    private final ChatMessageCommandService chatMessageCommandService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody ChatMessageCreateRequest request){
        chatMessageCommandService.createChatMessage(request);
        System.out.println("==========" + request.toString());
        return ResponseEntity.ok("채팅 메세지 생성!");
    }

    //사용자가 채팅방 열어서 읽음 처리
    @PostMapping("/mark-as-read")
    public ResponseEntity<String> markAsRead(@Valid @RequestBody MarkAsReadRequest request) {
        chatMessageCommandService.markChatRoomAsRead(request);
        return ResponseEntity.ok("채팅방을 읽음 처리했습니다.");
    }
}
