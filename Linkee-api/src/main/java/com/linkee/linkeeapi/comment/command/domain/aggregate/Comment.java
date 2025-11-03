package com.linkee.linkeeapi.comment.command.domain.aggregate;

import com.linkee.linkeeapi.common.enums.Status;
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


}
