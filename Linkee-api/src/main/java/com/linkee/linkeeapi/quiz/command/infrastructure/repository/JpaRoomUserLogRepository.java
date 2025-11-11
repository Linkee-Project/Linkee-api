package com.linkee.linkeeapi.quiz.command.infrastructure.repository;

import com.linkee.linkeeapi.quiz.command.domain.aggregate.RoomMember;
import com.linkee.linkeeapi.quiz.command.domain.aggregate.RoomQuestion;
import com.linkee.linkeeapi.quiz.command.domain.aggregate.RoomUserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface JpaRoomUserLogRepository extends JpaRepository<RoomUserLog, Long> {
    Optional<RoomUserLog> findByRoomMemberAndRoomQuestion(RoomMember roomMember, RoomQuestion roomQuestion);

    // ✅ 1. 특정 문제에 대한 제출 인원 수 카운트
    long countByRoomQuestion(RoomQuestion roomQuestion);

    // ✅ 2. 중복 제출 방지용 - 이미 제출했는지 확인
    boolean existsByRoomMemberAndRoomQuestion(RoomMember roomMember, RoomQuestion roomQuestion);
}
