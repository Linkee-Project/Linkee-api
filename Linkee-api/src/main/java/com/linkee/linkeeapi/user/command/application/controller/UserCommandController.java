package com.linkee.linkeeapi.user.command.application.controller;

import com.linkee.linkeeapi.auth.authService.AuthService;
import com.linkee.linkeeapi.common.security.model.CustomUser;
import com.linkee.linkeeapi.user.command.application.dto.request.UpdateUserNickNameRequest;
import com.linkee.linkeeapi.user.command.application.dto.request.UserCreateRequest;
import com.linkee.linkeeapi.user.command.application.service.UserCommandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "회원", description = "회원가입, 로그인, 계정, 프로필 관련 API")
@RequestMapping("/api/v1/users")
public class UserCommandController {

    private final UserCommandService userCommandService;


    @PatchMapping
    public ResponseEntity<String> updateUserNickName(@AuthenticationPrincipal CustomUser customUser,@RequestBody UpdateUserNickNameRequest request){


        userCommandService.updateNickname(customUser.getUserId(), request.getNickName());

        return ResponseEntity.ok("닉네임 변경 성공");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUserById(@AuthenticationPrincipal CustomUser customUser){

        userCommandService.deleteUser(customUser.getUserId());

        return ResponseEntity.ok("회원 삭제 성공(상태변경)");
    }
}
