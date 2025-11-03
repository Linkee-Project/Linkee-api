package com.linkee.linkeeapi.quiz_current_index.command.domain.aggregate;

import com.linkee.linkeeapi.quiz_room.command.domain.aggregate.QuizRoom;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_quiz_current_index")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizCurrentIndex {

    // PK이자 FK (tb_quiz_room.quiz_room_id)
    @Id
    @Column(name = "quiz_room_id")
    private Long quizRoomId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId // ← quizRoom의 id를 이 엔티티의 PK로 재사용
    @JoinColumn(name = "quiz_room_id", nullable = false, foreignKey = @ForeignKey(name = "FK_quizcurreuntindex_quizroom"))
    private QuizRoom quizRoom;

    // 현재 문제 인덱스 (NULL 허용)
    @Column(name = "current_quiz_index")
    private Integer currentQuizIndex;
}
