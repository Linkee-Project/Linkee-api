package com.linkee.linkeeapi.chat_member.query.mapper;
import com.linkee.linkeeapi.chat_member.query.dto.request.ChatMemberSearchRequest;
import com.linkee.linkeeapi.chat_member.query.dto.response.ChatMemberResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatMemberMapper {
    List<ChatMemberResponse> selectAllChatMember(ChatMemberSearchRequest request);

    int countChatMember(ChatMemberSearchRequest requestMapper);
}
