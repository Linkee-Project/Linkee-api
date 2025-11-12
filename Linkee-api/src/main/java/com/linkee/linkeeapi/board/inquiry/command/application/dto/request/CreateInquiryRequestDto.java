package com.linkee.linkeeapi.board.inquiry.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
/* 문의글 등록 요청 DTO */
public class CreateInquiryRequestDto {
    @NotBlank private String inquiryTitle;
    @NotBlank private String inquiryContent;
    private Long adminId;
}
