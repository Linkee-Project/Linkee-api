package com.linkee.linkeeapi.quiz_room.websocket.dto.response.data;

import com.linkee.linkeeapi.question.command.domain.aggregate.QuestionOption;
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

    public static OptionInfo from(QuestionOption e) {
        return OptionInfo.builder()
                .optionId(e.getQuestionOptionId())
                .optionIndex(e.getOptionIndex())
                .optionText(e.getOptionText())
                .build();
    }
}