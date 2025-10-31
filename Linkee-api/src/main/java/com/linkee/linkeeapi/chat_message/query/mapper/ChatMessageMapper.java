package com.linkee.linkeeapi.chat_message.query.mapper;

import com.linkee.linkeeapi.chat_message.query.dto.request.ChatMessageSearchRequest;
import com.linkee.linkeeapi.chat_message.query.dto.response.ChatMessageResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatMessageMapper {

    List<ChatMessageResponse> selectAllChatMessage(ChatMessageSearchRequest request);

    int countChatMessage(ChatMessageSearchRequest requestMapper);
}
