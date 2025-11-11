package com.linkee.linkeeapi.users.query.dto.response;


import lombok.Builder;
import lombok.Getter;


import java.time.LocalDateTime;
@Getter
@Builder
public class UserListResponse {


    private Long userId;
    private String userEmail;
    private String userPassword;
    private String userNickname;
    private String userStatus;
    private String userRole;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
