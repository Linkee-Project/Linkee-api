package com.linkee.linkeeapi.chat.query.mapper;

import com.linkee.linkeeapi.chat.query.dto.request.ChatMessageSearchRequest;
import com.linkee.linkeeapi.chat.query.dto.response.ChatMessageResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatMessageMapper {

    List<ChatMessageResponse> selectAllChatMessage(ChatMessageSearchRequest requestMapper);

    int countChatMessage(ChatMessageSearchRequest requestMapper);
}
