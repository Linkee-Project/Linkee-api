package com.linkee.linkeeapi.chat.command.application.controller.query_controller;

import com.linkee.linkeeapi.chat.command.domain.dto.query_dto.request.ChatMemberSearchRequest;
import com.linkee.linkeeapi.chat.command.domain.dto.query_dto.response.ChatMemberResponse;
import com.linkee.linkeeapi.chat.command.application.service.query_serivce.ChatMemberQueryService;
import com.linkee.linkeeapi.common.model.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat_members")
@Tag(name = "자율", description = "자율방(자유 출제방) 관련 API")
@Tag(name = "커뮤니케이션", description = "친구 및 채팅 기능 관련 API")
public class ChatMemberQueryController {

    private final ChatMemberQueryService chatMemberQueryService;

    // 채팅멤버 전체 리스트 조회 및 검색 페이징
    @GetMapping
    public PageResponse<ChatMemberResponse> selectAllChatMember(ChatMemberSearchRequest request){
        return chatMemberQueryService.selectAllChatMember(request);
    }

}
