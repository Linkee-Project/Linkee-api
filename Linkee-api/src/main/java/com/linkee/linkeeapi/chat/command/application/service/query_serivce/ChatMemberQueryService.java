package com.linkee.linkeeapi.chat.command.application.service.query_serivce;

import com.linkee.linkeeapi.chat.command.domain.dto.query_dto.request.ChatMemberSearchRequest;
import com.linkee.linkeeapi.chat.command.domain.dto.query_dto.response.ChatMemberResponse;
import com.linkee.linkeeapi.common.model.PageResponse;

public interface ChatMemberQueryService {

    PageResponse<ChatMemberResponse> selectAllChatMember(ChatMemberSearchRequest request);
}
