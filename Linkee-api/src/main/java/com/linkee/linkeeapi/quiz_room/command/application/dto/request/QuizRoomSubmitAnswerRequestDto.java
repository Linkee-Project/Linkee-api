package com.linkee.linkeeapi.quiz_room.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor // 답안 제출을 위한 DTO
public class QuizRoomSubmitAnswerRequestDto {
    //  현재 참여중인 퀴즈방 ID
    private Long quizRoomId;

    //  사용자가 선택한 보기의 인덱스(예: 1, 2, 3, 4)
    private Integer submittedOptionIndex;


}

