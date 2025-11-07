package com.linkee.linkeeapi.user.query.controller;

import com.linkee.linkeeapi.auth.authService.AuthService;
import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.user.query.dto.request.UserSearchRequest;
import com.linkee.linkeeapi.user.query.dto.response.UserListResponse;
import com.linkee.linkeeapi.user.query.dto.response.UserMeResponse;
import com.linkee.linkeeapi.user.query.service.UserQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "회원", description = "회원가입, 로그인, 계정, 프로필 관련 API")
public class UserQueryController {

    private final UserQueryService userQueryService;
    private final AuthService authService;


    @GetMapping
    public PageResponse<UserListResponse> selectAllUsers(UserSearchRequest request){

        return userQueryService.selectAllUsers(request);
    }


    @GetMapping("/me")
    public ResponseEntity<UserMeResponse> getUserMe(@AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = userDetails.getUsername();

        UserMeResponse response = userQueryService.getUserMe(userEmail);

        return ResponseEntity.ok(response);
    }

}
