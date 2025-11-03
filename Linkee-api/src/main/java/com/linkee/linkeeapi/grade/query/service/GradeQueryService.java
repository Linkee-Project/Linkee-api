package com.linkee.linkeeapi.grade.query.service;

import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.grade.query.dto.request.GradeSearchRequest;
import com.linkee.linkeeapi.grade.query.dto.response.GradeResponse;

import java.util.List;

public interface GradeQueryService {

    List<GradeResponse> selectAllGrade(GradeSearchRequest requestMapper);

}
