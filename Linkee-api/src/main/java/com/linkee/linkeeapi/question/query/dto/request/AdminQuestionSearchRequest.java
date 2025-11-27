package com.linkee.linkeeapi.question.query.dto.request;

import com.linkee.linkeeapi.common.enums.Status;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminQuestionSearchRequest {
    private String keyword;
    private Status verified; // Y, N, ALL (null)
    private Status deleted;  // Y, N, ALL (null)

    private Integer page;
    private Integer size;
    private Integer offset;
}
