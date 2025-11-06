package com.linkee.linkeeapi.chat_message.query.controller;

import com.linkee.linkeeapi.chat_message.query.dto.request.ChatMessageSearchRequest;
import com.linkee.linkeeapi.chat_message.query.dto.response.ChatMessageResponse;
import com.linkee.linkeeapi.chat_message.query.service.ChatMessageQueryService;
import com.linkee.linkeeapi.common.model.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat_messages")
@Tag(name = "자율", description = "자율방(자유 출제방) 관련 API")
@Tag(name = "커뮤니케이션", description = "친구 및 채팅 기능 관련 API")
public class ChatMessageQueryController {

    private final ChatMessageQueryService service;

    // 전체 목록 조회 및 페이징, 검색
    @GetMapping
    public PageResponse<ChatMessageResponse> selectAllChatMessage(ChatMessageSearchRequest request){

//        System.out.println("================================\n컨트롤러 " + request.getPage() + " " +request.getSize());
        return service.selectAllChatMessage(request);
    }

}
