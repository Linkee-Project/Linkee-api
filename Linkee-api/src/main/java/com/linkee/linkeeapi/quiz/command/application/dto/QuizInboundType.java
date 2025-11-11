package com.linkee.linkeeapi.quiz.command.application.dto;

//클라이언트 -> 서버
public enum QuizInboundType {
    START_QUIZ,
    SUBMIT_ANSWER,
    READY_TOGGLE,
    JOIN,
    LEAVE
}
