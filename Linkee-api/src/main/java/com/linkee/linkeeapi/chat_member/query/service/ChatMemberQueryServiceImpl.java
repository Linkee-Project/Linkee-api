package com.linkee.linkeeapi.chat_member.query.service;

import com.linkee.linkeeapi.chat_member.query.dto.request.ChatMemberSearchRequest;
import com.linkee.linkeeapi.chat_member.query.dto.response.ChatMemberResponse;
import com.linkee.linkeeapi.chat_member.query.mapper.ChatMemberMapper;
import com.linkee.linkeeapi.chat_message.command.application.dto.request.ChatMessageCreateRequest;
import com.linkee.linkeeapi.chat_message.query.dto.request.ChatMessageSearchRequest;
import com.linkee.linkeeapi.chat_message.query.dto.response.ChatMessageResponse;
import com.linkee.linkeeapi.common.model.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMemberQueryServiceImpl implements ChatMemberQueryService{

    private final ChatMemberMapper chatMemberMapper;

    @Override
    public PageResponse<ChatMemberResponse> selectAllChatMember(ChatMemberSearchRequest request){

        int page = request.getPage() != null && request.getPage() >= 0 ? request.getPage() : 0;
        int size = request.getSize() != null && request.getSize() > 0 ? request.getSize() : 10;
        int offset = page * size;

        // Mapper에 전달할 record 생성 (offset 포함)
        ChatMemberSearchRequest requestMapper = ChatMemberSearchRequest.builder()
                .userId(request.getUserId())
                .chatRoomId(request.getChatRoomId())
                .page(page)
                .size(size)
                .offset(offset)
                .build();


        List<ChatMemberResponse> results = chatMemberMapper.selectAllChatMember(requestMapper);


        int total = chatMemberMapper.countChatMember(requestMapper);

        return PageResponse.from(results, page, size, total);

    }

}
