package com.linkee.linkeeapi.chat.command.domain.repository;

import com.linkee.linkeeapi.chat.command.domain.entity.ChatMessage;
import com.linkee.linkeeapi.chat.chat_command.chat_domain.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
    Collection<ChatMessage> findByChatRoomOrderBySentAtAsc(ChatRoom room);
}
