package com.linkee.linkeeapi.room_member.query.dto.request;

import lombok.Builder;
import lombok.Getter;

/*
 * [기능] 퀴즈방 멤버 조회를 위한 요청 DTO
 * [역할] 클라이언트가 어떤 퀴즈방을 조회할지 정의합니다.
 */
@Getter
@Builder
public class RoomMemberSearchRequest {
    /* 검색할 퀴즈방 ID */
    private Long quizRoomId;
}
