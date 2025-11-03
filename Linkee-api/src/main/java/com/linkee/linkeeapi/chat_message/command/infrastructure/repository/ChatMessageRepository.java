package com.linkee.linkeeapi.chat_message.command.infrastructure.repository;

import com.linkee.linkeeapi.chat_member.command.domain.aggregate.entity.ChatMember;
import com.linkee.linkeeapi.chat_message.command.domain.aggregate.entity.ChatMessage;
import com.linkee.linkeeapi.chat_room.command.domain.aggregate.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {

}
