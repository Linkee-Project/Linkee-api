package com.linkee.linkeeapi.qna.query.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QnaResponseDto {

    private String qnaQuestion;
    private String qnaAnswer;

}
