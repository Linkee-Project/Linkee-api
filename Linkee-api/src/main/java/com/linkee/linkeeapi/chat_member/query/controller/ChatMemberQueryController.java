package com.linkee.linkeeapi.chat_member.query.controller;

import com.linkee.linkeeapi.chat_member.query.dto.request.ChatMemberSearchRequest;
import com.linkee.linkeeapi.chat_member.query.dto.response.ChatMemberResponse;
import com.linkee.linkeeapi.chat_member.query.service.ChatMemberQueryService;
import com.linkee.linkeeapi.common.model.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat_members")
public class ChatMemberQueryController {

    private final ChatMemberQueryService chatMemberQueryService;

    // 채팅멤버 전체 리스트 조회 및 검색 페이징
    @GetMapping
    public PageResponse<ChatMemberResponse> selectAllChatMember(ChatMemberSearchRequest request){
        return chatMemberQueryService.selectAllChatMember(request);
    }

}
