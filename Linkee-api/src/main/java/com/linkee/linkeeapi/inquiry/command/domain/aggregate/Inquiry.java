package com.linkee.linkeeapi.inquiry.command.domain.aggregate;

import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.user.model.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_inquiry")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_id")
    private Long inquiryId;

    @Column(name = "inquiry_title", nullable = false, length = 255)
    private String inquiryTitle;

    @Column(name = "inquiry_content", nullable = false, columnDefinition = "TEXT")
    private String inquiryContent;

    @Column(name = "answer_content", length = 255)
    private String answerContent;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();


    @Enumerated(EnumType.STRING)
    @Column(name = "answer_status", nullable = false)
    private Status answerStatus = Status.N;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "admin_id")
    private User admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
}
