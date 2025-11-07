package com.linkee.linkeeapi.chat.chat_command.chat_domain.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_messages")
public class ChatMessageMongo {
    @Id
    private String id;
    private Long roomId;
    private Long senderId;
    private String senderNickname;
    private String message;
    private LocalDateTime sentAt;
}
