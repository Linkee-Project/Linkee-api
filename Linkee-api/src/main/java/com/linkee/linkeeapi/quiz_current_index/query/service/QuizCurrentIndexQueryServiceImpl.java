package com.linkee.linkeeapi.quiz_current_index.query.service;

import com.linkee.linkeeapi.quiz_current_index.query.dto.response.CurrentQuestionResponse;
import com.linkee.linkeeapi.quiz_current_index.query.dto.response.QuizReconnectStateResponse;
import com.linkee.linkeeapi.quiz_current_index.query.mapper.QuizCurrentIndexMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizCurrentIndexQueryServiceImpl implements QuizCurrentIndexQueryService {

    private final QuizCurrentIndexMapper quizCurrentIndexMapper;

    @Override
    public int getCurrentIndex(Long roomId) {
        return quizCurrentIndexMapper.findCurrentIndexByRoomId(roomId);
    }

    @Override
    public CurrentQuestionResponse getCurrentQuestion(Long roomId) {
        return quizCurrentIndexMapper.findCurrentQuestion(roomId);
    }

    @Override
    public QuizReconnectStateResponse getReconnectState(Long roomId) {
        return quizCurrentIndexMapper.findReconnectState(roomId);
    }
}
