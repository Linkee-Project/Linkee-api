package com.linkee.linkeeapi.question_option.command.domain.aggregate;

import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.question.command.domain.aggregate.Question;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_question_option",
        uniqueConstraints = @UniqueConstraint(name = "uq_question_option_idx", columnNames = {"question_id", "question_option_index"}))
//UNIQUE 제약 이유 : 하나의 문제 안에서 option_index 값은 중복될 수 없음
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionOption {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_option_id")
    private Long questionOptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false, foreignKey = @ForeignKey(name = "FK_question_option_question"))
    private Question question;

    @Column(name = "question_option_index", nullable = false)
    private Integer optionIndex;

    @Column(name = "question_option_text", nullable = false, length = 255)
    private String optionText;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_corrected", nullable = false, length = 1)
    @Builder.Default
    private Status isCorrected = Status.N;

    public void updateOption(Integer newIndex, String newText) {
        this.optionIndex = newIndex;
        this.optionText = newText;
    }

    // 연관관계는 setter 자동 생성 지양
    /*양방향 세팅
    * FK를 가진 자식이(option) 부모(quetion)를 가리키도록 연결/해제
    * 항상 부모의 addOption/removeOption에서만 호출 (양방향 동기화)*/
    public void setQuestion(Question q) { this.question = q; }

    /* option의 정답 여부 업데이트
    * question_answer과 is_corrected가 항상 동기화 정답은 정확히 하나 규칙 유지 */
    public void setIsCorrected(Status s) { this.isCorrected = s; }
}

