package com.linkee.linkeeapi.user.query.service;

import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.user.query.dto.request.UserSearchRequest;
import com.linkee.linkeeapi.user.query.dto.response.UserListResponse;
import com.linkee.linkeeapi.user.query.dto.response.UserMeResponse;

public interface UserQueryService {

    PageResponse<UserListResponse> selectAllUsers(UserSearchRequest request);
    UserMeResponse getUserMe(String userEmail);
}
