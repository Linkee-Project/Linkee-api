package com.linkee.linkeeapi.quiz_room.query.dto.response;


import com.linkee.linkeeapi.category.command.aggregate.Category;
import com.linkee.linkeeapi.common.enums.RoomStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//  목록 조회 응답
public class QuizRoomListResponseDto {
    private String roomTitle;   // 방 제목
    private Category category;  // 카테고리
    private RoomStatus roomStatus;  // 상태 (대기, 진행)
    private Integer joinedCount;    // 현재 인원
    private Integer roomCapacity;   // 최대 인원

}
