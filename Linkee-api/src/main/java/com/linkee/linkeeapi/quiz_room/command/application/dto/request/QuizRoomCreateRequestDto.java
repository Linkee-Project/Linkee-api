package com.linkee.linkeeapi.quiz_room.command.application.dto.request;

import com.linkee.linkeeapi.common.enums.RoomMode;

import com.linkee.linkeeapi.common.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.NoArgsConstructor;

import lombok.Setter;



/*

 * 퀴즈룸 생성을 위한 요청 DTO (Data Transfer Object).

 * 클라이언트로부터 퀴즈룸 생성에 필요한 데이터를 전달받습니다.

 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizRoomCreateRequestDto {

    //  퀴즈룸이 속할 카테고리

    private Long categoryId;

    //  퀴즈룸의 제목.
    private String roomTitle;
    //  퀴즈룸의 모드 (예: SINGLE, GROUP).
    private RoomMode roomMode;
    //  퀴즈룸의 공개/비공개 여부 (Y: 비공개, N: 공개).
    private Status isPrivate;
    //  퀴즈룸에서 진행될 퀴즈의 총 개수.
    private Integer roomQuizLimit;
    // 퀴즈룸에 참여할 수 있는 최대 인원 수.
    private Integer roomCapacity;
    // 비공개 퀴즈룸일 경우 필요한 입장 코드. (isPrivate이 'Y'일 경우 사용)
    private Integer roomCode;
}
