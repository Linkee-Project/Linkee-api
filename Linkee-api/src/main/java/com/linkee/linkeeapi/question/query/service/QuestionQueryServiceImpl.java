package com.linkee.linkeeapi.question.query.service;

import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.question.query.dto.request.AdminQuestionSearchRequest; // AdminQuestionSearchRequest import
import com.linkee.linkeeapi.question.query.mapper.QuestionMapper;
import com.linkee.linkeeapi.question.query.dto.response.QuestionDetailResponseDto;
import com.linkee.linkeeapi.question.query.dto.response.QuestionListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionQueryServiceImpl implements QuestionQueryService {
    private final QuestionMapper questionMapper;

    //문제 목록 조회 + 옵션(keyword)
    @Override
    public PageResponse<QuestionListResponseDto> getQuestionList(int page, Integer size, String keyword) {

        int pageSize = (size != null) ? size : 10;
        int offset = page * pageSize;

        String k = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;

        List<QuestionListResponseDto> questions =
                questionMapper.findAllWithKeyword(k, offset, pageSize);
        int total = questionMapper.countAllWithKeyword(k);

        return PageResponse.from(questions, page, pageSize, total);
    }
    //문제 카테고리별 조회 + 옵션(keyword)
    @Override
    public PageResponse<QuestionListResponseDto> getQuestionsListByCategory(int page, Integer size, Long categoryId, String keyword) {

        int pageSize = (size != null) ? size : 10;
        int offset = page * pageSize;

        String k = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;

        List<QuestionListResponseDto> questions =
                questionMapper.findByCategoryWithKeyword(categoryId, k, offset, pageSize);

        int total = questionMapper.countByCategoryWithKeyword(categoryId, k);

        return PageResponse.from(questions,page,pageSize,total);
    }

    @Override
    public PageResponse<QuestionListResponseDto> getQuestionsByCurrentUser(Long userId, int page, Integer size, String keyword) {
        int pageSize = (size != null) ? size : 10;
        int offset = page * pageSize;

        String k = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;

        List<QuestionListResponseDto> questions =
                questionMapper.findByCurrentUserWithKeyword(userId, k, offset, pageSize);
        int total = questionMapper.countByCurrentUserWithKeyword(userId, k);

        return PageResponse.from(questions, page, pageSize, total);
    }

    //문제 상세 조회
    @Override
    @Transactional
    public QuestionDetailResponseDto getQuestionDetail(Long questionId) {
        questionMapper.increaseViewCount(questionId);

        // 문제 상세 조회 id 검증
        QuestionDetailResponseDto detail = questionMapper.findDetailByQuestionId(questionId);
        if (detail == null) throw new BusinessException(ErrorCode.QUESTION_NOT_FOUND);

        // 옵션 목록 조회
        List<QuestionDetailResponseDto.OptionList> options = questionMapper.findDetailOptions(questionId);

        // DTO에 세팅
        detail.setOptions(options);

        return detail;
    }

    // 관리자용 문제 목록 조회 구현
    @Override
    public PageResponse<QuestionListResponseDto> getAdminQuestionList(AdminQuestionSearchRequest request) {
        int pageSize = (request.getSize() != null) ? request.getSize() : 10;
        int offset = (request.getPage() != null ? request.getPage() : 0) * pageSize;

        // AdminQuestionSearchRequest에 offset 설정
        request.setOffset(offset);
        request.setSize(pageSize); // Mapper에서 size를 사용하므로 설정

        List<QuestionListResponseDto> questions = questionMapper.findAdminQuestions(request);
        int total = questionMapper.countAdminQuestions(request);

        return PageResponse.from(questions, request.getPage() != null ? request.getPage() : 0, pageSize, total);
    }

}
