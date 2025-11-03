package com.linkee.linkeeapi.user.query.mapper;

import com.linkee.linkeeapi.user.query.dto.request.UserSearchRequest;
import com.linkee.linkeeapi.user.query.dto.response.UserListResponse;
import com.linkee.linkeeapi.user.query.dto.response.UserMeResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    List<UserListResponse> selectAllUser(UserSearchRequest request);

    int countUser(UserSearchRequest requestMapper);

    UserMeResponse selectUserMe(@Param("userEmail") String userEmail);

}
