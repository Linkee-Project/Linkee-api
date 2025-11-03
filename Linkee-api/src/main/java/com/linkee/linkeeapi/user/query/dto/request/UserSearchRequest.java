package com.linkee.linkeeapi.user.query.dto.request;

import com.linkee.linkeeapi.common.enums.Role;
import com.linkee.linkeeapi.common.enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserSearchRequest {

    private String keyword;
    private Integer page;
    private Integer size;
    private Integer offset;

    private Long userId;
    private String userNickname;
    private String userStatus;
    private String userRole;



}
