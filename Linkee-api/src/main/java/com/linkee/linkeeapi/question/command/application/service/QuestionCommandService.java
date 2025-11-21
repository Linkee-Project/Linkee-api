package com.linkee.linkeeapi.question.command.application.service;

import com.linkee.linkeeapi.question.command.application.dto.request.CreateQuestionRequestDto;
import com.linkee.linkeeapi.question.command.application.dto.request.UpdateQuestionRequestDto;

public interface QuestionCommandService {
    void createQuestion(CreateQuestionRequestDto request,Long userId);

    void updateQuestion(Long questionId, UpdateQuestionRequestDto request,Long userId);


    void deleteQuestion(Long questionId, Long userId);

    void verifyQuestion(Long questionId, Long adminId );

    void adminDeleteQuestion(Long questionId, Long adminId);
}
