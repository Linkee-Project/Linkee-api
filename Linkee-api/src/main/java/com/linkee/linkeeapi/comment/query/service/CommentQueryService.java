package com.linkee.linkeeapi.comment.query.service;

import com.linkee.linkeeapi.comment.query.dto.CommentListResponseDto;
import com.linkee.linkeeapi.common.model.PageResponse;

import java.util.List;

public interface CommentQueryService {


    List<CommentListResponseDto> listAllForQuestion(Long questionId);

    List<CommentListResponseDto> listChildren(Long parentId);

}
