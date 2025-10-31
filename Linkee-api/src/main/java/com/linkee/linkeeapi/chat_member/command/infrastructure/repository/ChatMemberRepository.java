package com.linkee.linkeeapi.chat_member.command.infrastructure.repository;

import com.linkee.linkeeapi.chat_member.command.domain.aggregate.entity.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMemberRepository extends JpaRepository<ChatMember,Long> {
}
