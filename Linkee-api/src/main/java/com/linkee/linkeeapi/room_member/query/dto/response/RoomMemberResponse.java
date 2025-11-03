package com.linkee.linkeeapi.room_member.query.dto.response;

import lombok.*;
import java.time.LocalDateTime;

/*
 * [기능] 퀴즈방 멤버 조회 API의 '응답' 데이터 규격
 * [역할] 조회된 단일 멤버의 상세 정보를 담아 클라이언트에게 전달합니다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomMemberResponse {
    private Long roomMemberId;  // 멤버의 고유 ID
    private Long memberId;      // 유저의 고유 ID (조회 결과에는 포함)
    private Long quizRoomId;    // 속한 퀴즈방의 ID
    private String isReady;     // 준비 상태
    private String isVictory;   // 승리 여부
    private LocalDateTime joinedAt; // 참여 시각
    private LocalDateTime leftAt;   // 나간 시각
}