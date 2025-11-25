package com.linkee.linkeeapi.users.command.application.controller;

import com.linkee.linkeeapi.common.model.CustomUser;
import com.linkee.linkeeapi.users.command.application.dto.request.DeleteUserRequest;
import com.linkee.linkeeapi.users.command.application.dto.request.UpdateUserNickNameRequest;
import com.linkee.linkeeapi.users.command.application.dto.request.UpdateUserRoleRequest;
import com.linkee.linkeeapi.users.command.application.service.UserCommandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "회원", description = "회원가입, 로그인, 계정, 프로필 관련 API")
@RequestMapping("/api/v1")
public class UserCommandController {

    private final UserCommandService userCommandService;


    @PatchMapping("/users/user")
    public ResponseEntity<String> updateUserNickName(@AuthenticationPrincipal CustomUser customUser,@RequestBody UpdateUserNickNameRequest request){


        userCommandService.updateNickname(customUser.getUserId(), request.getNickName());

        return ResponseEntity.ok("닉네임 변경 성공");
    }

    @PatchMapping("/admin/users/user/role")
    public ResponseEntity<?> updateUserRole(
            @RequestBody UpdateUserRoleRequest request
    ) {
        userCommandService.updateUserRole(request);
        return ResponseEntity.ok("권한 변경 완료");
    }

    @DeleteMapping("/admin/users/user/delete")
    public ResponseEntity<?> deleteUserAdmin(@RequestBody DeleteUserRequest request
    ) {
        userCommandService.deleteUser(request.getUserId());
        return ResponseEntity.ok("유저 삭제 성공(비활성화)");
    }

    @DeleteMapping("/users/user/delete")
    public ResponseEntity<String> deleteUserById(@AuthenticationPrincipal CustomUser customUser){

        userCommandService.deleteUser(customUser.getUserId());

        return ResponseEntity.ok("회원 삭제 성공(상태변경)");
    }
}
