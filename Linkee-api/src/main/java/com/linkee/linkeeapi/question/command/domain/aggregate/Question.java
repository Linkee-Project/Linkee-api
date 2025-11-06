package com.linkee.linkeeapi.question.command.domain.aggregate;

import com.linkee.linkeeapi.category.command.aggregate.Category;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.common.model.BaseTimeEntity;
import com.linkee.linkeeapi.question.command.application.dto.request.UpdateQuestionRequestDto;
import com.linkee.linkeeapi.question_option.command.domain.aggregate.QuestionOption;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Table(name = "tb_question")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long questionId;

    @Column(name = "question_title", length = 100, nullable = false)
    private String questionTitle;

    @Lob
    @Column(name = "question_question", nullable = false, columnDefinition = "TEXT")
    private String questionQuestion;

    @Column(name = "question_answer", nullable = false)
    private Integer questionAnswer;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_qualified", nullable = false, columnDefinition = "ENUM('Y','N') DEFAULT 'N'")
    private Status isQualified = Status.N;

    @Column(name = "question_views")
    private Long questionViews;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_Deleted", nullable = false, columnDefinition = "ENUM('Y','N') DEFAULT 'N'")
    private Status isDeleted = Status.N;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "category_id", nullable = false, foreignKey = @ForeignKey(name = "FK_question_category"))
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_question_user"))
    private User user;

    //옵션 컬렉션
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<QuestionOption> options = new ArrayList<>();

    /* 부모(Question) ↔ 자식(QuestionOption) 양방향 연관관계를 항상 동기화시키는 메서드
     * 문제 등록(생성) 과정에서 4개의 보기(option)를 추가할 때 사용 */
    public void addOption(QuestionOption option) {
        if (option == null) return;
        options.add(option);
        option.setQuestion(this);
    }

    //제목 변경
    public void changeTitle(String newTitle) {
        if (newTitle == null || newTitle.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "문제 제목은 필수 입력 값입니다.");
        }
        this.questionTitle = newTitle;
    }

    //내용 변경
    public void changeQuestion(String newQuestion) {
        if (newQuestion == null || newQuestion.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "문제 내용은 필수 입력 값입니다.");
        }
        this.questionQuestion = newQuestion;
    }

    /* 정답 변경 메서드
     * 호출 시점
     * 1. 사용자가 검증 전 상태의 문제를 수정할 때
     * 2. 서비스 계층에서 isQualified 상태를 먼저 검증한 뒤 호출됨 */
    public void changeAnswer(int newAnswer) {
        boolean exists = options.stream()
                .anyMatch(o -> o.getOptionIndex().equals(newAnswer));
        if (!exists) {
            throw new BusinessException(ErrorCode.INVALID_ANSWER_INDEX);
        }
        this.questionAnswer = newAnswer;
        // 컬렉션이 로딩되어 있다면 동기화
        for (QuestionOption o : options) {
            o.setIsCorrected(
                    o.getOptionIndex().equals(newAnswer) ? Status.Y : Status.N
            );
        }
    }

    public void softDelete() {
        this.isDeleted = Status.Y;
    }


    // 삭제 권한 검증
    public void assertDeletableBy(User requestUser, Long ownerId) {
        if (!requestUser.getUserId().equals(ownerId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_QUESTION_ACCESS);
        }
        if (this.isQualified == Status.Y) {
            throw new BusinessException(ErrorCode.QUESTION_DELETE_NOT_ALLOWED);
        }
    }

    // 수정 권한 검증
    public void assertUpdatableBy(User requestUser, Long ownerId) {
        if (!requestUser.getUserId().equals(ownerId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_QUESTION_ACCESS);
        }
        if (this.isQualified == Status.Y) {
            throw new BusinessException(ErrorCode.QUESTION_UPDATE_NOT_ALLOWED);
        }
    }


    /*문제 검증 완료 처리 메서드
     * 관리자가 문제의 상태를 검증상태 변경할 때 사용.*/
    public void verifyByAdmin(User admin) {
        if (this.isQualified == Status.Y) {
            throw new BusinessException(ErrorCode.QUESTION_ALREADY_QUALIFIED);
        }
        this.isQualified = Status.Y;

    }
}
