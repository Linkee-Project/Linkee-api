package com.linkee.linkeeapi.quiz.domain.dto.command_dto.response.data;

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
}