package com.linkee.linkeeapi.relation.command.domain.aggregate.entity;

import com.linkee.linkeeapi.common.model.BaseTimeEntity;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_relation")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Relation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relation_id")
    private Long relationId;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "relation_status", nullable = false, length = 1)
    private RelationStatus relationStatus = RelationStatus.P; // 기본값


    @LastModifiedDate
    @Column(name = "accepted_at", insertable = false)
    private LocalDateTime acceptedAt;

    // 요청을 받은 사용자 (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    // 요청을 보낸 사용 (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    public void modifyRelationStatus(RelationStatus status) {
        this.relationStatus = status;
    }
}
