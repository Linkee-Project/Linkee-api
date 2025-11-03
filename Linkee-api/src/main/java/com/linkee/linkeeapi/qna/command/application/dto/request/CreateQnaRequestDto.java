package com.linkee.linkeeapi.qna.command.application.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateQnaRequestDto {
    private String question;
    private String answer;
    private Long roomId;
    private Long chatMemberId; // ✅ 수정
}

