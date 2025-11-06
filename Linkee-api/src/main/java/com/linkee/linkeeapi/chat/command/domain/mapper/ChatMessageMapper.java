package com.linkee.linkeeapi.chat.command.domain.mapper;

import com.linkee.linkeeapi.chat.command.domain.dto.query_dto.request.ChatMessageSearchRequest;
import com.linkee.linkeeapi.chat.command.domain.dto.query_dto.response.ChatMessageResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatMessageMapper {

    List<ChatMessageResponse> selectAllChatMessage(ChatMessageSearchRequest requestMapper);

    int countChatMessage(ChatMessageSearchRequest requestMapper);
}
