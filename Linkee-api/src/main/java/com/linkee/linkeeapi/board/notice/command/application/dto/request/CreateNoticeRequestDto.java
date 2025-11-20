package com.linkee.linkeeapi.board.notice.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class CreateNoticeRequestDto {
    @NotBlank String noticeTitle;
    @NotBlank String noticeContent;
}
