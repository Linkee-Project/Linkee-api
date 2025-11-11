package com.linkee.linkeeapi.users.query.mapper;

import com.linkee.linkeeapi.users.query.dto.request.UserGradeSearchRequest;
import com.linkee.linkeeapi.users.query.dto.response.UserGradeResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserGradeMapper {
    List<UserGradeResponse> selectAllUserGrades(UserGradeSearchRequest request);
    int countUserGrades(UserGradeSearchRequest request);
}
