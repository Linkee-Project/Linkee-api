package com.linkee.linkeeapi.chat.chat_command.chat_domain.dto.response;

import com.linkee.linkeeapi.chat.chat_command.chat_domain.entity.ChatRoomType;
import com.linkee.linkeeapi.common.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRoomResponseDto {
    private Long chatRoomId;
    private String chatRoomName;
    private ChatRoomType chatRoomType;
    private Status isPrivate;
    private Integer joinedCount;
}