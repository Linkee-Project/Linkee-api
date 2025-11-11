package com.linkee.linkeeapi.quiz.command.domain.aggregate;

import com.linkee.linkeeapi.common.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_room_user_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomUserLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_user_log_id")
    private Long roomUserLogId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_question_id", nullable = false, foreignKey = @ForeignKey(name = "FK_room_user_log_question"))
    private RoomQuestion roomQuestion;

    // 방 멤버 FK
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_member_id", nullable = false, foreignKey = @ForeignKey(name = "FK_room_user_log_member")
    )
    private RoomMember roomMember;

    // 정답 여부 (NULL 허용)
    @Enumerated(EnumType.STRING)
    @Column(name = "is_corrected")
    private Status isCorrected;
}