package com.linkee.linkeeapi.users.query.service;

import com.linkee.linkeeapi.users.query.dto.request.GradeSearchRequest;
import com.linkee.linkeeapi.users.query.dto.response.GradeResponse;
import com.linkee.linkeeapi.users.query.mapper.GradeMapper;
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
