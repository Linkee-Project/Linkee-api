package com.linkee.linkeeapi.notice.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateNoticeRequestDto {
    @NotBlank String noticeTitle;
    @NotBlank String noticeContent;
    @NotBlank Long adminId; //작성자 ID
}
