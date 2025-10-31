package com.linkee.linkeeapi.chat_message.query.service;

import com.linkee.linkeeapi.chat_message.query.dto.request.ChatMessageSearchRequest;
import com.linkee.linkeeapi.chat_message.query.dto.response.ChatMessageResponse;
import com.linkee.linkeeapi.chat_message.query.mapper.ChatMessageMapper;
import com.linkee.linkeeapi.common.model.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageQueryServiceImpl implements ChatMessageQueryService {

    private final ChatMessageMapper mapper;

    @Override
    public PageResponse<ChatMessageResponse> selectAllChatMessage(ChatMessageSearchRequest request) {

        int page = request.getPage() != null && request.getPage() >= 0 ? request.getPage() : 0;
        int size = request.getSize() != null && request.getSize() > 0 ? request.getSize() : 10;
        int offset = page * size;

        // Mapper에 전달할 record 생성 (offset 포함)
        ChatMessageSearchRequest requestMapper = new ChatMessageSearchRequest(
                request.getKeyword(),
                page,
                size,
                offset,
                request.getChatRoomId(),
                request.getSenderId()
        );

        List<ChatMessageResponse> results = mapper.selectAllChatMessage(requestMapper);


        int total = mapper.countChatMessage(requestMapper);

        return PageResponse.from(results, page, size, total);
    }



}
