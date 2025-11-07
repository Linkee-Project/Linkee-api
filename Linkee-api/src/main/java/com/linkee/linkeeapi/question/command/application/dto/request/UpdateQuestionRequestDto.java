package com.linkee.linkeeapi.question.command.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private Long userId;
    private String questionTitle;
    private String questionQuestion;
    private Integer questionAnswer;
    @Valid
    private List<UpdateOption> options;     //수정할 보기 목록

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateOption {
        @NotNull
        private Integer optionIndex;
        @NotBlank
        private String optionText;
    }

}
