package com.linkee.linkeeapi.chat_message.command.application.service;

import com.linkee.linkeeapi.chat_message.command.application.dto.request.ChatMessageCreateRequest;
import com.linkee.linkeeapi.chat_message.command.application.dto.request.MarkAsReadRequest;

public interface ChatMessageCommandService {

    void createChatMessage(ChatMessageCreateRequest request);

    void markChatRoomAsRead(MarkAsReadRequest request);
}
