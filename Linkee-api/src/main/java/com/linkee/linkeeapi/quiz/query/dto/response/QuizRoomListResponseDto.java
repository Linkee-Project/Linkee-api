package com.linkee.linkeeapi.quiz.query.dto.response;


import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.question.command.domain.aggregate.Category;
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
    private String categoryName;   // 카테고리
    private Long categoryId;
    private RoomStatus roomStatus;  // 상태 (대기, 진행)
    private Integer joinedCount;    // 현재 인원
    private Integer roomCapacity;   // 최대 인원
    private Long quizRoomId;        // stomp 구현시 필요
    private Status isPrivate;
}
