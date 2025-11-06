package com.linkee.linkeeapi.chat.command.application.service.query_serivce;

import com.linkee.linkeeapi.chat.command.domain.dto.query_dto.request.ChatMessageSearchRequest;
import com.linkee.linkeeapi.chat.command.domain.dto.query_dto.response.ChatMessageResponse;
import com.linkee.linkeeapi.chat.command.domain.mapper.ChatMessageMapper;
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

        int page = request.getPage() != null && request.getPage() > 0 ? request.getPage() : 0;
        int size = request.getSize() != null && request.getSize() > 0 ? request.getSize() : 10;
        int offset = page * size;

        // Mapper에 전달할 record 생성 (offset 포함)
        ChatMessageSearchRequest requestMapper = ChatMessageSearchRequest.builder()
                .keyword(request.getKeyword())
                .page(page)
                .size(size)
                .offset(offset)
                .chatRoomId(request.getChatRoomId())
                .senderId(request.getSenderId())
                .build();


        List<ChatMessageResponse> results = mapper.selectAllChatMessage(requestMapper);

//        System.out.println("=================\n" +
//                request.getPage() + " " +request.getSize());

        int total = mapper.countChatMessage(requestMapper);

        return PageResponse.from(results, page, size, total);
    }



}
