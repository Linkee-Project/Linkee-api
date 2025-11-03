package com.linkee.linkeeapi.qna.command.domain.aggregate;

import com.linkee.linkeeapi.chat_member.command.domain.aggregate.entity.ChatMember;
import com.linkee.linkeeapi.chat_room.command.domain.aggregate.ChatRoom;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_qna")
public class Qna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qnaId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String qnaQuestion;
    private String qnaAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id" ,referencedColumnName = "chat_room_id")
    ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_member_id",referencedColumnName = "chat_member_id")
    ChatMember chatMember;


}
