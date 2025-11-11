package com.linkee.linkeeapi.chat.command.application.service.services;

import com.linkee.linkeeapi.chat.command.application.dto.request.ChatMemberCreateRequest;
import com.linkee.linkeeapi.chat.command.application.dto.response.ChatMemberCreateResponse;
import com.linkee.linkeeapi.chat.command.application.dto.response.ChatMemberDeleteResponse;

public interface ChatMemberCommandService {

    ChatMemberCreateResponse createChatMember(ChatMemberCreateRequest request);
    void updateIsRead(Long chatMemberId);
    ChatMemberDeleteResponse deleteChatMember(Long userId, Long chatRoomId);
}
