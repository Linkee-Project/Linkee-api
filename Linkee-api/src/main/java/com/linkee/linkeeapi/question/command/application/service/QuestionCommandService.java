package com.linkee.linkeeapi.question.command.application.service;

import com.linkee.linkeeapi.question.command.application.dto.request.CreateQuestionRequestDto;
import com.linkee.linkeeapi.question.command.application.dto.request.UpdateQuestionRequestDto;
import com.linkee.linkeeapi.question.command.application.dto.request.VerifyQuestionRequestDto;

public interface QuestionCommandService {
    void createQuestion(CreateQuestionRequestDto request);

    void updateQuestion(Long questionId, UpdateQuestionRequestDto request);


    void deleteQuestion(Long questionId, Long userId);

    void verifyQuestion(Long questionId, VerifyQuestionRequestDto request);
}
