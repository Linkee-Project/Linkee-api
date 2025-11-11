package com.linkee.linkeeapi.users.query.service;

import com.linkee.linkeeapi.users.query.dto.request.GradeSearchRequest;
import com.linkee.linkeeapi.users.query.dto.response.GradeResponse;

import java.util.List;

public interface GradeQueryService {

    List<GradeResponse> selectAllGrade(GradeSearchRequest requestMapper);

}
