package com.linkee.linkeeapi.inquiry.query.dto.response;

import com.linkee.linkeeapi.common.enums.Status;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryResponseDto {

    private String inquiryTitle;
    private String inquiryContent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String answerContent;
    private Status answerStatus;
    private Long userId;
    private Long adminId;

}
