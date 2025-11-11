package com.linkee.linkeeapi.quiz.command.application.dto;

// 서버 → 클라이언트
public enum QuizMessageType {

    QUESTION_STARTED,    // 문제 시작 (30초 타이머)
    ANSWER_SUBMITTED,     // 답안 접수 확인
    QUESTION_RESULT,     // 문제 결과 (30초 후)
    QUIZ_FINISHED,       // 퀴즈 종료
    MEMBER_UPDATED,      // 멤버 목록 갱신 (준비 상태 변경 시)
    ERROR                // 에러
}
