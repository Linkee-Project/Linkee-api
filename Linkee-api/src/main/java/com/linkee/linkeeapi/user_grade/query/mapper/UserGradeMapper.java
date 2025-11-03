package com.linkee.linkeeapi.user_grade.query.mapper;

import com.linkee.linkeeapi.user_grade.query.dto.request.UserGradeSearchRequest;
import com.linkee.linkeeapi.user_grade.query.dto.response.UserGradeResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserGradeMapper {
    List<UserGradeResponse> selectAllUserGrades(UserGradeSearchRequest request);
    int countUserGrades(UserGradeSearchRequest request);
}
