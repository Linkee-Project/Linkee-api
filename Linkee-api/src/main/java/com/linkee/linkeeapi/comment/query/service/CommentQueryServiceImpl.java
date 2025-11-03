package com.linkee.linkeeapi.comment.query.service;

import com.linkee.linkeeapi.comment.query.dto.CommentListResponseDto;
import com.linkee.linkeeapi.comment.query.mapper.CommentMapper;
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

    @Override
    public List<CommentListResponseDto> listChildren(Long parentId) {
        return mapper.findChildCommentsByParentId(parentId, Status.N);
    }
}
