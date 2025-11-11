package com.linkee.linkeeapi.chat.query.service;

import com.linkee.linkeeapi.chat.query.dto.request.ChatMessageSearchRequest;
import com.linkee.linkeeapi.chat.query.dto.response.ChatMessageResponse;
import com.linkee.linkeeapi.common.model.PageResponse;


public interface ChatMessageQueryService {

    PageResponse<ChatMessageResponse> selectAllChatMessage(ChatMessageSearchRequest request);
}
