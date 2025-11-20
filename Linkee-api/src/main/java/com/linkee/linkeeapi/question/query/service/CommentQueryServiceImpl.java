package com.linkee.linkeeapi.question.query.service;

import com.linkee.linkeeapi.question.query.dto.response.CommentListResponseDto;
import com.linkee.linkeeapi.question.query.mapper.CommentMapper;
import com.linkee.linkeeapi.common.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentQueryServiceImpl implements CommentQueryService {
    private final CommentMapper mapper;

    @Override
    public List<CommentListResponseDto> listAllForQuestion(Long questionId) {
        return mapper.findAllByQuestionIdWithUser(questionId, Status.N);
    }

}
