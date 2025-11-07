package com.linkee.linkeeapi.room_member.command.application.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/*
 * 룸 멤버 생성을 위한 요청 DTO (Data Transfer Object).
 * 클라이언트로부터 룸 멤버 생성에 필요한 정보를 받습니다.
 */
@Setter
@Getter
@Builder
public class RoomMemberCreateRequest {

     // 룸 멤버가 참여할 퀴즈룸의 ID.
    private Long quizRoomId;

     // 룸 멤버로 참여할 사용자의 ID.
    private Long userId;

    // 비공개방 입장 시 필요한 룸 코드
    private String roomCode;
}