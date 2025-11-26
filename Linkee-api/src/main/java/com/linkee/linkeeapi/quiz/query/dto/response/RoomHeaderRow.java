package com.linkee.linkeeapi.quiz.query.dto.response;

import lombok.*;

/*
 *  - 방 상세 조회 시, "방 제목/방장" 같은 헤더 정보만 가져오기 위한 경량 DTO.
 *  - 멤버 목록은 기존 RoomMemberMapper를 재사용해서 가져온다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomHeaderRow {
    private Long quizRoomId;     // tb_quiz_room.quiz_room_id
    private String roomTitle;    // tb_quiz_room.room_title
    private Long ownerId;        // tb_quiz_room.room_owner
    private String ownerNickname;// tb_user.user_nickname (room_owner 조인)

    private String categoryName;   // 카테고리 이름
    private Integer roomQuizLimit; // 문제 수
    private Integer roomCapacity;  // 최대 인원
}
