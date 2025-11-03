package com.linkee.linkeeapi.quiz_room.command.domain.aggregate;

import com.linkee.linkeeapi.category.command.aggregate.Category;
import com.linkee.linkeeapi.common.enums.RoomMode;
import com.linkee.linkeeapi.common.enums.RoomStatus;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.model.BaseTimeEntity;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "tb_quiz_room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(name = "FK_quiz_room_category"))
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_owner", nullable = false, foreignKey = @ForeignKey(name = "FK_quiz_room_room_owner"))
    private User roomOwner;

    @Column(name = "room_title", nullable = false, length = 255)
    private String roomTitle;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_status", nullable = false, columnDefinition = "ENUM('W', 'P', 'E') DEFAULT 'W'")
    private RoomStatus roomStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_mode", nullable = false, columnDefinition = "ENUM('S', 'G')")
    private RoomMode roomMode;

    @Column(name = "joined_count")
    private Integer joinedCount;

    @Column(name = "room_capacity")
    private Integer roomCapacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_private", nullable = false, columnDefinition = "ENUM('Y', 'N') DEFAULT 'Y'")
    private Status isPrivate;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @Column(name = "room_code")
    private Integer roomCode;

    @Column(name = "room_quiz_limit", nullable = false)
    private Integer roomQuizLimit;


}
