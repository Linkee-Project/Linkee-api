package com.linkee.linkeeapi.room_user_log.command.infrastructure.repository;

import com.linkee.linkeeapi.room_member.command.domain.aggregate.RoomMember;
import com.linkee.linkeeapi.room_question.command.domain.aggregate.RoomQuestion;
import com.linkee.linkeeapi.room_user_log.command.domain.aggregate.RoomUserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface JpaRoomUserLogRepository extends JpaRepository<RoomUserLog, Long> {
    Optional<RoomUserLog> findByRoomMemberAndRoomQuestion(RoomMember roomMember, RoomQuestion roomQuestion);
}
