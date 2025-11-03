package com.linkee.linkeeapi.user.query.controller;

import com.linkee.linkeeapi.auth.authService.AuthService;
import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.user.query.dto.request.UserSearchRequest;
import com.linkee.linkeeapi.user.query.dto.response.UserListResponse;
import com.linkee.linkeeapi.user.query.dto.response.UserMeResponse;
import com.linkee.linkeeapi.user.query.service.UserQueryService;
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

        System.out.println("컨트롤러에서 호출:" + userDetails.getUsername() + userDetails.getAuthorities());

        return ResponseEntity.ok(response);
    }

}
