package com.linkee.linkeeapi.chat_member.command.domain.aggregate.entity;

import com.linkee.linkeeapi.chat_room.command.domain.aggregate.ChatRoom;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_chat_member")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class ChatMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_member_id")
    private Long chatMemberId;

    @CreatedDate
    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    @Column(name = "left_at")
    private LocalDateTime leftAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false, foreignKey = @ForeignKey(name = "FK_chatmember_chatroom"))
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_chatmember_user"))
    private User user;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "is_read", nullable = false, columnDefinition = "ENUM('Y','N') DEFAULT 'N'")
    private Status isRead = Status.N;


    public void modifyIsRead(){
        this.isRead = Status.Y;
    }

    public void modifyLeftAt() {
        this.leftAt = LocalDateTime.now();
    }

    //읽음 여부 로직추가
    public void setIsRead(Status status) {
        this.isRead = status;
    }
}
