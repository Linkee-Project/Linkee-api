package com.linkee.linkeeapi.chat_message.command.application.controller;

import com.linkee.linkeeapi.chat_message.command.application.dto.request.ChatMessageCreateRequest;
import com.linkee.linkeeapi.chat_message.command.application.service.ChatMessageCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat_messages")
public class ChatMessageCommandController {

    private final ChatMessageCommandService chatMessageCommandService;

    @PostMapping
    public ResponseEntity<String> createChatRoom(@RequestBody ChatMessageCreateRequest request){
        chatMessageCommandService.createChatMessage(request);
        System.out.println("==========" + request.toString());
        return ResponseEntity.ok("채팅 메세지 생성!");
    }

}
