package com.linkee.linkeeapi.chat.chat_command.chat_domain.entity;

import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.model.BaseTimeEntity;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_chat_room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long chatRoomId;

    @Column(name = "chat_room_name", nullable = false, length = 50)
    private String chatRoomName;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "chat_room_type", nullable = false, columnDefinition = "ENUM('GAME','CHAT')")
    private ChatRoomType chatRoomType = ChatRoomType.CHAT;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "is_private", nullable = false, columnDefinition = "ENUM('Y','N') DEFAULT 'Y'")
    private Status isPrivate = Status.N;

    @Column(name = "room_code")
    private Integer roomCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_owner", nullable = false, foreignKey = @ForeignKey(name = "FK_chatroom_owner"))
    private User roomOwner;

    @Column(name = "joined_count", nullable = false)
    @Builder.Default
    private Integer joinedCount = 1;

    @Column(name = "room_capacity")
    private Integer roomCapacity;


    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "room_status", nullable = false, columnDefinition = "ENUM('Y','N') DEFAULT 'Y'" )
    private Status roomStatus = Status.Y;


    public void decreaseJoinedCount() {
        this.joinedCount = this.joinedCount - 1;
        if (this.joinedCount <= 0) {
            this.roomStatus = Status.N;
        }
    }

    public void increaseJoinedCount() {
        this.joinedCount++;
    }


    public void closeRoom() {
        this.roomStatus = Status.N;
    }
}
