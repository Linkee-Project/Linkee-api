package com.linkee.linkeeapi.question.query.service;


import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.question.query.dto.request.AdminQuestionSearchRequest;
import com.linkee.linkeeapi.question.query.dto.response.QuestionDetailResponseDto;
import com.linkee.linkeeapi.question.query.dto.response.QuestionListResponseDto;

public interface QuestionQueryService {

    PageResponse<QuestionListResponseDto> getQuestionList(int page, Integer size, String keyword);

    PageResponse<QuestionListResponseDto> getQuestionsListByCategory(int page, Integer size, Long categoryId, String keyword);

    QuestionDetailResponseDto getQuestionDetail(Long questionId);

    PageResponse<QuestionListResponseDto> getQuestionsByCurrentUser(Long userId, int page, Integer size, String keyword);

    // 관리자용 문제 목록 조회
    PageResponse<QuestionListResponseDto> getAdminQuestionList(AdminQuestionSearchRequest request);
}
