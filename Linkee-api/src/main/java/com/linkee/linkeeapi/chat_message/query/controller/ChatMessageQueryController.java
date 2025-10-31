package com.linkee.linkeeapi.chat_message.query.controller;

import com.linkee.linkeeapi.chat_message.query.dto.request.ChatMessageSearchRequest;
import com.linkee.linkeeapi.chat_message.query.dto.response.ChatMessageResponse;
import com.linkee.linkeeapi.chat_message.query.service.ChatMessageQueryService;
import com.linkee.linkeeapi.common.model.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat_messages")
public class ChatMessageQueryController {

    private final ChatMessageQueryService service;

    // 전체 목록 조회 및 페이징, 검색
    @GetMapping
    public PageResponse<ChatMessageResponse> selectAllChatMessage(ChatMessageSearchRequest request){

        return service.selectAllChatMessage(request);
    }

}
