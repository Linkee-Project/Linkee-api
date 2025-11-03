package com.linkee.linkeeapi.question.query.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDetailResponseDto {
    private Long questionId;
    private String questionTitle;
    private String content;
    private Integer questionAnswer;
    private String categoryName;
    private String userNickname;
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<OptionList> options;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OptionList {
        private Long optionId;
        private Integer optionIndex;
        private String optionText;
        private String isCorrected;
    }

}
