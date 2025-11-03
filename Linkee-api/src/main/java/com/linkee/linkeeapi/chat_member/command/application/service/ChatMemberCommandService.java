package com.linkee.linkeeapi.chat_member.command.application.service;

import com.linkee.linkeeapi.chat_member.command.application.dto.reqeust.ChatMemberCreateRequest;
import com.linkee.linkeeapi.chat_member.command.application.dto.response.ChatMemberCreateResponse;

public interface ChatMemberCommandService {

    ChatMemberCreateResponse createChatMember(ChatMemberCreateRequest request);
    void updateIsRead(Long chatMemberId);
    String deleteChatMember(Long chatMemberId);
}
