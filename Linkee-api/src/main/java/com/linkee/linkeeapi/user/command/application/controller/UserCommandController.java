package com.linkee.linkeeapi.user.command.application.controller;

import com.linkee.linkeeapi.auth.authService.AuthService;
import com.linkee.linkeeapi.user.command.application.dto.request.UpdateUserNickNameRequest;
import com.linkee.linkeeapi.user.command.application.dto.request.UserCreateRequest;
import com.linkee.linkeeapi.user.command.application.service.UserCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserCommandController {

    private final UserCommandService userCommandService;



    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserCreateRequest request) {

        userCommandService.createUser(request);
        return ResponseEntity.ok("회원가입 완료!");
    }

    @PatchMapping
    public ResponseEntity<String> updateUserNickName(@RequestBody UpdateUserNickNameRequest request){

        userCommandService.updateNickname(request);

        return ResponseEntity.ok("닉네임 변경 성공");
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long userId){
        userCommandService.deleteUser(userId);
        return ResponseEntity.ok("회원 삭제 성공(상태변경)");
    }
}
