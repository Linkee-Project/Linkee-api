package com.linkee.linkeeapi.quiz.command.application.dto.response.data;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptionInfo {
    private Long optionId;
    private String optionText;
    private Integer optionIndex;

    public static OptionInfo from(com.linkee.linkeeapi.question_option.command.domain.aggregate.QuestionOption e) {
        return OptionInfo.builder()
                .optionId(e.getQuestionOptionId())
                .optionIndex(e.getOptionIndex())
                .optionText(e.getOptionText())
                .build();
    }
}