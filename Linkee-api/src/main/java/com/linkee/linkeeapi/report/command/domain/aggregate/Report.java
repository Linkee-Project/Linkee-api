package com.linkee.linkeeapi.report.command.domain.aggregate;

import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.model.BaseTimeEntity;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_report")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @Column(name = "report_title", nullable = false, length = 255)
    private String reportTitle;

    @Column(name = "report_content", nullable = false, length=999)
    private String reportContent;

    @Column(name = "report_action", length = 999)
    private String reportAction;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_status", nullable = false, columnDefinition = "ENUM('Y','N') DEFAULT 'N'")
    private Status reportStatus = Status.N;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_type", nullable = false, columnDefinition = "ENUM('C','B','R','U','G')")
    private ReportType reportType;

    //신고자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false, foreignKey = @ForeignKey(name = "FK_report_reporter"))
    private User reportUser;

    //신고 대상자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_id", nullable = false, foreignKey = @ForeignKey(name = "FK_report_reported"))
    private User reportedUser;

    // 처리한 관리자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", foreignKey = @ForeignKey(name = "FK_report_admin"))
    private User admin;

    // Report.java 내부
    public void handleReport(User admin, String action) {
        this.admin = admin;
        this.reportAction = action;
        this.reportStatus = Status.Y; // 처리 완료 상태로 변경
    }
}

