package com.linkee.linkeeapi.grade.query.service;

import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.grade.query.controller.GradeQueryController;
import com.linkee.linkeeapi.grade.query.dto.request.GradeSearchRequest;
import com.linkee.linkeeapi.grade.query.dto.response.GradeResponse;
import com.linkee.linkeeapi.grade.query.mapper.GradeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeQueryServiceImpl implements GradeQueryService {

    private final GradeMapper gradeMapper;


    @Override
    public List<GradeResponse> selectAllGrade(GradeSearchRequest request) {

        return gradeMapper.selectAllGrade(request);
    }


}
