package com.linkee.linkeeapi.question.query.service;

import com.linkee.linkeeapi.question.query.dto.response.CommentListResponseDto;

import java.util.List;

public interface CommentQueryService {


    List<CommentListResponseDto> listAllForQuestion(Long questionId);


}
