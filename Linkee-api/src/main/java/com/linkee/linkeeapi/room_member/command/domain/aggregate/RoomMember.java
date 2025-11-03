package com.linkee.linkeeapi.room_member.command.domain.aggregate;

import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.quiz_room.command.domain.aggregate.QuizRoom;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 룸 멤버 엔티티. 퀴즈룸에 참여하는 사용자의 정보를 관리합니다.
 * `tb_room_member` 테이블과 매핑됩니다.
 */
@Entity
@Table(name = "tb_room_member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomMember {
    /*
     * 룸 멤버의 고유 ID (Primary Key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomMemberId;

    /*
     * 룸 멤버에 해당하는 사용자 정보 (User 엔티티와 Many-to-One 관계).
     * `tb_user` 테이블의 `user_id`와 매핑됩니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "FK_room_member_member_id"))
    private User member;

    /*
     * 룸 멤버가 참여하고 있는 퀴즈룸 정보 (QuizRoom 엔티티와 Many-to-One 관계).
     * `tb_quiz_room` 테이블의 `quiz_room_id`와 매핑됩니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_room_id", nullable = false, foreignKey = @ForeignKey(name = "FK_room_member_quiz_room_id"))
    private QuizRoom quizroom;

    /*
     * 룸 멤버의 준비 상태 (Y: 준비, N: 미준비).
     * `ENUM('Y','N')` 타입으로 데이터베이스에 저장됩니다.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "is_ready", nullable = false, columnDefinition = "ENUM('Y','N') DEFAULT 'N'")
    private Status isReady;

    /*
     * 룸 멤버의 승리 여부 (Y: 승리, N: 패배, NULL: 미정).
     * `ENUM('Y','N')` 타입으로 데이터베이스에 저장됩니다.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "is_victory", columnDefinition = "ENUM('Y','N')")
    private Status isVictory;

    /*
     * 룸에 참여한 시간.
     * 기본값은 현재 타임스탬프입니다.
     */
    @Column(name = "joined_at", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime joinedAt;

    /*
     * 룸에서 나간 시간.
     * 사용자가 룸을 나갈 경우 업데이트됩니다.
     */
    @Column(name = "lefted_at")
    private LocalDateTime leftedAt;
}
