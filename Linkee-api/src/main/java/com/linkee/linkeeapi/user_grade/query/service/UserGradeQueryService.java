package com.linkee.linkeeapi.user_grade.query.service;

import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.user_grade.query.dto.request.UserGradeSearchRequest;
import com.linkee.linkeeapi.user_grade.query.dto.response.UserGradeResponse;
import com.linkee.linkeeapi.user_grade.query.mapper.UserGradeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserGradeQueryService {

    private final UserGradeMapper userGradeMapper;

    public PageResponse<UserGradeResponse> selectAllUserGrades(UserGradeSearchRequest request) {

        int page = (request.getPage() != null && request.getPage() > 0) ? request.getPage() : 0;
        int size = (request.getSize() != null && request.getSize() > 0) ? request.getSize() : 10;
        int offset = page * size;

        UserGradeSearchRequest requestMapper = UserGradeSearchRequest.builder()
                .userId(request.getUserId())
                .categoryId(request.getCategoryId())
                .gradeId(request.getGradeId())
                .keyword(request.getKeyword())
                .page(page)
                .size(size)
                .offset(offset)
                .build();

        List<UserGradeResponse> results = userGradeMapper.selectAllUserGrades(requestMapper);
        int total = userGradeMapper.countUserGrades(requestMapper);

        return PageResponse.from(results, page, size, total);
    }
}
