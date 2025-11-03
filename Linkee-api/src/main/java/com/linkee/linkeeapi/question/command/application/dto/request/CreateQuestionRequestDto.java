package com.linkee.linkeeapi.question.command.application.dto.request;

import com.linkee.linkeeapi.user.command.domain.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
public class CreateQuestionRequestDto {

    @NotNull
    private Long userId; //작성자 ID

    @NotNull
    private Long categoryId;

    @NotBlank
    @Size(max = 100)
    private String questionTitle;

    @NotBlank
    private String questionQuestion;

    @NotNull
    private Integer questionAnswer;

    @NotNull
    @Size(min = 1)
    @Valid
    private List<OptionDto> options;

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OptionDto {

        @NotNull
        private Integer index;

        @NotBlank
        @Size(max = 255)
        private String text;
    }
}
