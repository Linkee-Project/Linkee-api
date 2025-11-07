package com.linkee.linkeeapi.chat.command.application.service.query_serivce;

import com.linkee.linkeeapi.chat.command.domain.dto.query_dto.request.ChatMemberSearchRequest;
import com.linkee.linkeeapi.chat.command.domain.dto.query_dto.response.ChatMemberResponse;
import com.linkee.linkeeapi.chat.command.domain.mapper.ChatMemberMapper;
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

        int page = request.getPage() != null && request.getPage() > 0 ? request.getPage() : 0;
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
