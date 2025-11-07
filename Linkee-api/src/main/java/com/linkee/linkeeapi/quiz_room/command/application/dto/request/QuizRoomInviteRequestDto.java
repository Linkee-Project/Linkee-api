package com.linkee.linkeeapi.quiz_room.command.application.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizRoomInviteRequestDto {
    //  초대를 보내는 사용자 ID
    private Long inviterId;

    //  초대를 받는 사용자 ID
    private Long inviteeId;

    //  초대할 퀴즈방 ID
    private Long quizRoomId;
}
