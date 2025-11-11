package com.linkee.linkeeapi.users.query.dto.request;

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
