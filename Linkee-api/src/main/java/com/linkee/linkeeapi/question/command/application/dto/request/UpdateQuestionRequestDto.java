package com.linkee.linkeeapi.question.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateQuestionRequestDto {
    private Long userId;
    private String questionId;
    private String questionTitle;
    private String questionQuestion;
    private Integer questionAnswer;
    private List<UpdateOption> options;     //수정할 보기 목록

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateOption {
        private Long optionId;
        private Integer optionIndex;
        private String optionText;
    }

}
