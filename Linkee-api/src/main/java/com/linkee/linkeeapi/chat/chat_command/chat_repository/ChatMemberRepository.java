package com.linkee.linkeeapi.chat.chat_command.chat_repository;

import com.linkee.linkeeapi.chat.chat_command.chat_domain.entity.ChatMember;
import com.linkee.linkeeapi.chat.chat_command.chat_domain.entity.ChatRoom;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ChatMemberRepository extends JpaRepository<ChatMember,Long> {
    Optional<ChatMember> findByChatRoomAndUser(ChatRoom chatRoom, User user);

    @Query("SELECT m FROM ChatMember m JOIN FETCH m.user WHERE m.chatRoom = :room AND m.leftAt IS NULL")
    List<ChatMember> findByChatRoomAndLeftAtIsNull(@Param("room") ChatRoom room);

    long countByChatRoom(ChatRoom chatRoom);
    List<ChatMember> findByChatRoom(ChatRoom chatRoom);
    boolean existsByChatRoomAndUser(ChatRoom chatRoom, User user);

    void deleteByChatRoomAndUser(ChatRoom room, User user);

    Optional<ChatMember> findByChatRoomAndUser_UserId(ChatRoom chatRoom, Long userId);

}
