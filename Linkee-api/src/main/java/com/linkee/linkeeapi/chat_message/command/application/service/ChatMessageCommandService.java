package com.linkee.linkeeapi.chat_message.command.application.service;

import com.linkee.linkeeapi.chat_message.command.application.dto.request.ChatMessageCreateRequest;

public interface ChatMessageCommandService {

    void createChatMessage(ChatMessageCreateRequest request);
}
