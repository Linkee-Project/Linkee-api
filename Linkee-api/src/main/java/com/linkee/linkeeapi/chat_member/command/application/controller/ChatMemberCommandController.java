package com.linkee.linkeeapi.chat_member.command.application.controller;

import com.linkee.linkeeapi.chat_member.command.application.dto.reqeust.ChatMemberCreateRequest;
import com.linkee.linkeeapi.chat_member.command.application.dto.response.ChatMemberCreateResponse;
import com.linkee.linkeeapi.chat_member.command.application.service.ChatMemberCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat_members")
public class ChatMemberCommandController {

    private final ChatMemberCommandService chatMemberCommandService;


    // create
    @PostMapping
    public ResponseEntity<String> createChatMember(@RequestBody ChatMemberCreateRequest request) {

        ChatMemberCreateResponse response = chatMemberCommandService.createChatMember(request);

        String message = "[" +response.getChatRoomId() +" | " + response.getChatRoomName() +"] 방 "
        + response.getUserNickName() + " 님이 입장하셨습니다.";

        return ResponseEntity.ok(message);
    }

    //updateIsRead
    @PatchMapping("/read/{chatMemberId}")
    public ResponseEntity<String> updateIsRead(@PathVariable Long chatMemberId) {
        chatMemberCommandService.updateIsRead(chatMemberId);
        return ResponseEntity.ok("읽음 처리 완료");
    }

    //deleteChatMember -> 나가면 leftAt 시간을 update처리
    @DeleteMapping("/leave/{chatMemberId}")
    public ResponseEntity<String> deleteChatMember(@PathVariable Long chatMemberId) {
        String result = chatMemberCommandService.deleteChatMember(chatMemberId);
        return ResponseEntity.ok(result +" 님이 퇴장하셨습니다");
    }

}
