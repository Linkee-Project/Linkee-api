package com.linkee.linkeeapi.room_member.command.infrastructure.repository;

import com.linkee.linkeeapi.quiz_room.command.domain.aggregate.QuizRoom;
import com.linkee.linkeeapi.room_member.command.domain.aggregate.RoomMember;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {
    // 특정 QuizRoom에 속한 모든 RoomMember를 조회하는 메서드
    List<RoomMember> findByQuizRoom(QuizRoom quizRoom);
    // 사용자가 이미 방에 참가하였는지 확인 (중복 방지)
    boolean existsByQuizRoomAndMember(QuizRoom quizRoom, User member);
    // 특정 QuizRoom과 User에 해당하는 RoomMember를 조회하는 메서드
    Optional<RoomMember> findByQuizRoomAndMember(QuizRoom quizRoom, User member);

}