package com.linkee.linkeeapi.quiz.domain.dto.command_dto.response.data;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizQuestionData {
    private Integer questionNumber;    // 5
    private Integer totalQuestions;    // 5/10

    private Long questionId;
    private String questionContent;
    private String categoryName;
    private String difficulty;

    private List<OptionInfo> options;

    private LocalDateTime serverStartTime;
    private Integer timeLimit;
}