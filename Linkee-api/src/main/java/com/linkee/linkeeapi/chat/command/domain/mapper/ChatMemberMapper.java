package com.linkee.linkeeapi.chat.command.domain.mapper;
import com.linkee.linkeeapi.chat.command.domain.dto.query_dto.request.ChatMemberSearchRequest;
import com.linkee.linkeeapi.chat.command.domain.dto.query_dto.response.ChatMemberResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatMemberMapper {
    List<ChatMemberResponse> selectAllChatMember(ChatMemberSearchRequest request);

    int countChatMember(ChatMemberSearchRequest requestMapper);
}
