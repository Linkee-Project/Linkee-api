package com.linkee.linkeeapi.room_member.command.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class RoomMemberCreateResponse {
    private Long quizRoomId;
    private String quizRoomName;
    private String userNickName;
}