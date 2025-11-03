package com.linkee.linkeeapi.room_member.command.infrastructure.repository;

import com.linkee.linkeeapi.room_member.command.domain.aggregate.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {
}