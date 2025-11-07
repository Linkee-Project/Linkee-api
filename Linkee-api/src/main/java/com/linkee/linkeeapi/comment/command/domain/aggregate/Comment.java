package com.linkee.linkeeapi.comment.command.domain.aggregate;

import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.common.model.BaseTimeEntity;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.question.command.domain.aggregate.Question;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "comment_content", length = 999, nullable = false)
    private String commentContent;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false, foreignKey = @ForeignKey(name = "FK_comment_question"))
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_comment_user"))
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name="is_Deleted",  nullable = false, columnDefinition = "ENUM('Y','N') DEFAULT 'N'")
    private Status isDeleted = Status.N;

    // 부모 댓글 (대댓글인 경우에만 존재)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id", foreignKey = @ForeignKey(name = "FK_comment_parent"))
    private Comment parent;

    // 자식 댓글들(대댓글 리스트)
    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE,   // 부모 삭제 시 자식들 먼저 제거(JPA가 순서 보장)
            orphanRemoval = true)
    @OrderBy("commentId ASC")
    @Builder.Default
    private List<Comment> children = new ArrayList<>();

    /*최상위 댓글(루트 댓글)을 생성*/
    public static Comment createRoot(Question q, User u, String content) {

        if (content == null || content.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST,"댓글 내용은 비어 있을 수 없습니다.");
        }

        Comment root = Comment.builder()
                .question(q)
                .user(u)
                .commentContent(content)
                .isDeleted(Status.N)
                .build();
        return root;
    }

    /*대댓글 생성
     *[댓글 구조 예시]
     * 문제
     *  ㄴ 댓글 A (루트)  ← 이것에만 대댓글 가능
     *      ㄴ 대댓글 B  ← 여기에는 대댓글 불가능 */
    public static Comment createReply(Question q, User u, Comment parent, String content) {
        // [검증 1] 대댓글의 대댓글 금지
        if (parent.getParent() != null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST,"대댓글은 1단계까지만 허용됩니다.");
        }
        // [검증 2] 부모와 같은 Question만 허용
        // 예: "질문1의 댓글"에 "질문2의 대댓글"을 다는 것 방지
        if (!parent.getQuestion().getQuestionId().equals(q.getQuestionId())) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST,"부모 댓글과 다른 문제에는 대댓글을 달 수 없습니다.");
        }

        // 대댓글 생성
        Comment child = Comment.builder()
                .question(q)
                .user(u)
                .commentContent(content)
                .isDeleted(Status.N)
                .build();

        // 부모 댓글에 이 대댓글을 연결 (parent ↔ child 양쪽 모두 연결)
        parent.addChild(child);

        return child;
    }
    //댓글 수정
    public void updateContent(String newContent) {
        if (newContent == null || newContent.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST,"댓글 내용은 비어 있을 수 없습니다.");
        }
        this.commentContent = newContent;
    }

    // 추가(등록/대댓글 연결)
    public void addChild(Comment child) {
        this.children.add(child);  // 부모 컬렉션 동기화
        child.setParent(this);     // 자식 FK 동기화(연관관계 주인)
    }

    // 제거(연결 해제/삭제 전 처리)
    public void removeChild(Comment child) {
        this.children.remove(child); // 부모 컬렉션 동기화
        child.setParent(null);       // 자식 FK 해제 → orphan 제거 트리거
    }

    //softDelete
    public void softDelete() {
        this.isDeleted = Status.Y;
        this.commentContent = "(삭제된 댓글입니다)";
    }



}
