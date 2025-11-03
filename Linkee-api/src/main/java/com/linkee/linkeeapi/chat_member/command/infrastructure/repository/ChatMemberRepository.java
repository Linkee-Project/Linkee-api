package com.linkee.linkeeapi.chat_member.command.infrastructure.repository;

import com.linkee.linkeeapi.chat_member.command.domain.aggregate.entity.ChatMember;
import com.linkee.linkeeapi.chat_room.command.domain.aggregate.ChatRoom;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMemberRepository extends JpaRepository<ChatMember,Long> {
    Optional<ChatMember> findByChatRoomAndUser(ChatRoom chatRoom, User user);
    long countByChatRoom(ChatRoom chatRoom);
    List<ChatMember> findAllByChatRoom(ChatRoom chatRoom);
}
