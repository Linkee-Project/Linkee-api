package com.linkee.linkeeapi.quiz_room.command.application.service;


import com.linkee.linkeeapi.quiz_room.command.application.dto.request.QuizRoomInviteRequestDto;

public interface QuizRoomInviteService {
    /* 퀴즈방 초대 관련 요청 처리
    * 친구 관계 확인, 방 정보 조회 후 초대 알림 보내기
    * @param request 초대 요청 정보 담은 DTO */
    void sendInvite(QuizRoomInviteRequestDto request);
}
