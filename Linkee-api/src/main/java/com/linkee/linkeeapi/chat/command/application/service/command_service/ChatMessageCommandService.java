package com.linkee.linkeeapi.chat.command.application.service.command_service;

import com.linkee.linkeeapi.chat.command.domain.dto.command_dto.request.ChatMessageCreateRequest;
import com.linkee.linkeeapi.chat.command.domain.dto.command_dto.request.MarkAsReadRequest;

public interface ChatMessageCommandService {

    void createChatMessage(ChatMessageCreateRequest request);

    void markChatRoomAsRead(MarkAsReadRequest request);
}
