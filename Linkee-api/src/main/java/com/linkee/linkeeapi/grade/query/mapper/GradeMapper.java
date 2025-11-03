package com.linkee.linkeeapi.grade.query.mapper;

import com.linkee.linkeeapi.grade.query.dto.request.GradeSearchRequest;
import com.linkee.linkeeapi.grade.query.dto.response.GradeResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GradeMapper {
    List<GradeResponse> selectAllGrade(GradeSearchRequest request);

    int countGrade(GradeSearchRequest requestMapper);

}
