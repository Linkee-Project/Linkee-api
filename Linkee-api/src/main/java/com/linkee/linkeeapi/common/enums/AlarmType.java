package com.linkee.linkeeapi.common.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlarmType {
    // 친구
    FRIEND_REQUEST("FRIEND_REQUEST", "친구 요청 알림"),
    // 퀴즈
    QUIZ_INVITE("QUIZ_INVITE", "퀴즈방 초대 알림"),
    // 문의
    INQUIRY_ANSWERED("INQUIRY_ANSWERED", "문의 답변 등록 알림"),
    // 문제
    QUESTION_VERIFIED("QUESTION_VERIFIED", "문제 검증 완료 알림"),
    // 댓글
    NEW_COMMENT("NEW_COMMENT", "댓글 생성 알림"),
    NEW_REPLY("NEW_REPLY", "대댓글 생성 알림");

    private final String code;
    private final String description;
}
