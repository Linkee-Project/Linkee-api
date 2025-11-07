package com.linkee.linkeeapi.quiz_current_index.query.service;

import com.linkee.linkeeapi.quiz_current_index.query.dto.response.CurrentQuestionResponse;
import com.linkee.linkeeapi.quiz_current_index.query.dto.response.QuizReconnectStateResponse;

public interface QuizCurrentIndexQueryService {
    /* 현재 문제 인덱스 반환 */
    int getCurrentIndex(Long roomId);

    /* 현재 문제 내용 반환 */
    CurrentQuestionResponse getCurrentQuestion(Long roomId);

    /*  재접속 시 전체 상태 복원용 정보 반환 */
    QuizReconnectStateResponse getReconnectState(Long roomId);
}
