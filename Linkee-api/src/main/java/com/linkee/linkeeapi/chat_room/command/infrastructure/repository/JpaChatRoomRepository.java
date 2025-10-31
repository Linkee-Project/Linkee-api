package com.linkee.linkeeapi.chat_room.command.infrastructure.repository;

import com.linkee.linkeeapi.chat_room.command.domain.aggregate.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
