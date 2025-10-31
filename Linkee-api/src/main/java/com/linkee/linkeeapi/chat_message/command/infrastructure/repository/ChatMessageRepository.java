package com.linkee.linkeeapi.chat_message.command.infrastructure.repository;

import com.linkee.linkeeapi.chat_message.command.domain.aggregate.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
}
