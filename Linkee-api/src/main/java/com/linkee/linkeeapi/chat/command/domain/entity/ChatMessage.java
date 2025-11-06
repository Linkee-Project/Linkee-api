package com.linkee.linkeeapi.chat.command.domain.entity;

import com.linkee.linkeeapi.chat.chat_command.chat_domain.entity.ChatRoom;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "tb_chat_message")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatMessageId;

    @Column(name = "message_content", columnDefinition = "TEXT", nullable = false)
    private String messageContent;

    @CreatedDate
    @Column(name = "sent_at", nullable = false, updatable = false)
    private LocalDateTime sentAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", referencedColumnName = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", referencedColumnName = "user_id")
    private User sender;


}
