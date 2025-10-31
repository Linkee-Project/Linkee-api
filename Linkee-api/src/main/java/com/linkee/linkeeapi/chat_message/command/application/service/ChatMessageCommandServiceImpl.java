package com.linkee.linkeeapi.chat_message.command.application.service;

import com.linkee.linkeeapi.chat_message.command.application.dto.request.ChatMessageCreateRequest;
import com.linkee.linkeeapi.chat_message.command.domain.aggregate.entity.ChatMessage;
import com.linkee.linkeeapi.chat_message.command.infrastructure.repository.ChatMessageRepository;
import com.linkee.linkeeapi.chat_room.command.domain.aggregate.ChatRoom;
import com.linkee.linkeeapi.chat_room.command.domain.aggregate.ChatRoomType;
import com.linkee.linkeeapi.chat_room.command.domain.repository.ChatRoomRepository;
import com.linkee.linkeeapi.chat_room.command.infrastructure.repository.JpaChatRoomRepository;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.user.model.entity.User;
import com.linkee.linkeeapi.user.service.util.UserFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageCommandServiceImpl implements ChatMessageCommandService{

    private final ChatMessageRepository repository;
    private final UserFinder userFinder;
    private final JpaChatRoomRepository chatRoomRepository;

    @Override
    public void createChatMessage(ChatMessageCreateRequest request) {

        User foundUser = userFinder.getById(request.getSenderId());
        ChatRoom foundRoom = chatRoomRepository.findById(request.getChatRoomId()).orElseThrow();

        ChatMessage message = ChatMessage.builder()
                .messageContent(request.getMessageContent())
                .chatRoom(foundRoom)
                .sender(foundUser)
                .build();

        repository.save(message);
    }


}
