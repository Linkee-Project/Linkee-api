package com.linkee.linkeeapi.chat_message.query.service;

import com.linkee.linkeeapi.chat_message.query.dto.request.ChatMessageSearchRequest;
import com.linkee.linkeeapi.chat_message.query.dto.response.ChatMessageResponse;
import com.linkee.linkeeapi.common.model.PageResponse;


public interface ChatMessageQueryService {

    PageResponse<ChatMessageResponse> selectAllChatMessage(ChatMessageSearchRequest request);
}
