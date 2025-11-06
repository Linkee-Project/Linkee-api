package com.linkee.linkeeapi.chat.chat_command.chat_repository;

import com.linkee.linkeeapi.chat.chat_command.chat_domain.entity.ChatRoom;
import com.linkee.linkeeapi.common.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByChatRoomIdAndRoomStatus(Long chatRoomId, Status status);
    List<ChatRoom> findByRoomStatus(Status status);

}
