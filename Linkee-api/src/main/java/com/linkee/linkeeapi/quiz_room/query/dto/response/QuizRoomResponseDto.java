package com.linkee.linkeeapi.quiz_room.query.dto.response;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//  빠른 시작 응답
public class QuizRoomResponseDto {
    private Long quizRoomId;      // 퀴즈방 ID
    private String roomTitle;     // 제목
    private Integer joinedCount;   // 현재 인원
    private Integer roomCapacity;  // 최대 인원
}
