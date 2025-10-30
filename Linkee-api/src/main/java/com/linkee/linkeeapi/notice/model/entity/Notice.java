package com.linkee.linkeeapi.notice.model.entity;

import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.model.BaseTimeEntity;
import com.linkee.linkeeapi.user.model.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_notice")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long noticeId;

    @Column(name = "notice_title", nullable = false, length =255)
    private String noticeTitle;

    @Column(name = "notice_content", nullable = false, length = 999)
    private String noticeContent;

    @Column(name = "notice_views")
    private Long noticeViews = 0L;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_deleted", nullable = false, columnDefinition = "ENUM('Y','N') DEFAULT 'N'")
    private Status isDeleted = Status.N;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false, foreignKey = @ForeignKey(name = "FK_notice_admin"))
    private User admin;

    public void assignAdmin(User admin) {
        if (admin == null) {
            throw new IllegalArgumentException("관리자는 null일 수 없습니다.");
        }
        this.admin = admin;
    }

    public void updateNotice(String title, String content) {
        if (title != null && !title.isBlank()) {
            this.noticeTitle = title;
        }
        if (content != null && !content.isBlank()) {
            this.noticeContent = content;
        }
    }

    public void deleteNotice() {
        this.isDeleted = Status.Y;
    }

}
