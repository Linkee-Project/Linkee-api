package com.linkee.linkeeapi.chat_member.query.service;

import com.linkee.linkeeapi.chat_member.query.dto.request.ChatMemberSearchRequest;
import com.linkee.linkeeapi.chat_member.query.dto.response.ChatMemberResponse;
import com.linkee.linkeeapi.common.model.PageResponse;

public interface ChatMemberQueryService {

    PageResponse<ChatMemberResponse> selectAllChatMember(ChatMemberSearchRequest request);
}
