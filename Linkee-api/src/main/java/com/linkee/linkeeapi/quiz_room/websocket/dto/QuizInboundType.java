package com.linkee.linkeeapi.quiz_room.websocket.dto;

//클라이언트 -> 서버
public enum QuizInboundType {
    START_QUIZ,
    SUBMIT_ANSWER,
    READY_TOGGLE,
    JOIN,
    LEAVE
}
