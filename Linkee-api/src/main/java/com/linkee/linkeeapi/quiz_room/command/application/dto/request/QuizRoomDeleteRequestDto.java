package com.linkee.linkeeapi.quiz_room.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class QuizRoomDeleteRequestDto {
    private Long quizRoomId;
    private Long userId;
}
