package com.linkee.linkeeapi.room_question.command.application.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomQuestionCreateRequest {
    private Long quizRoomId;
    private Long questionId;
    private Integer quizOrder;  // 퀴즈방에서 해당 문제가 몇 번째 문제로 출제되는지 나타내는 값
}
