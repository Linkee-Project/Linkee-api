package com.linkee.linkeeapi.chat.command.application.service.services;

import com.linkee.linkeeapi.chat.command.application.dto.request.ChatMessageCreateRequest;
import com.linkee.linkeeapi.chat.command.application.dto.request.MarkAsReadRequest;

public interface ChatMessageCommandService {

    void createChatMessage(ChatMessageCreateRequest request);

    void markChatRoomAsRead(MarkAsReadRequest request);
}
