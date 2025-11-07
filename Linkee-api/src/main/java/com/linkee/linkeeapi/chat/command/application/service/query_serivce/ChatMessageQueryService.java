package com.linkee.linkeeapi.chat.command.application.service.query_serivce;

import com.linkee.linkeeapi.chat.command.domain.dto.query_dto.request.ChatMessageSearchRequest;
import com.linkee.linkeeapi.chat.command.domain.dto.query_dto.response.ChatMessageResponse;
import com.linkee.linkeeapi.common.model.PageResponse;


public interface ChatMessageQueryService {

    PageResponse<ChatMessageResponse> selectAllChatMessage(ChatMessageSearchRequest request);
}
