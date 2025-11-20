package com.linkee.linkeeapi.board.notice.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNoticeRequestDto {
    private String noticeTitle;
    private String noticeContent;
}
