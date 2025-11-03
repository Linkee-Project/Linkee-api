package com.linkee.linkeeapi.user_grade.command.application.controller;


import com.linkee.linkeeapi.user_grade.command.application.dto.request.UpdateVictoryCountRequest;
import com.linkee.linkeeapi.user_grade.command.application.dto.request.UserGradeCreateRequest;
import com.linkee.linkeeapi.user_grade.command.application.service.UserGradeCommandService;
import com.linkee.linkeeapi.user_grade.command.domain.entity.UserGrade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user_grades")
public class UserGradeCommandController {

    private final UserGradeCommandService userGradeCommandService;

    // 이거 현재는 유저 생성될때 카테고리 갯수만큼 기본등급 같이 생성되게
    // 유저쪽에 만들어놓음
    // 추후 카테고리가 더 생성되면 임의로 추가를 위해 남겨둠
    @PostMapping
    public ResponseEntity<UserGrade> createUserGrade(@RequestBody UserGradeCreateRequest request) {

        return ResponseEntity.ok(userGradeCommandService.createUserGrade(request));

    }


    @PatchMapping("/victory")
    public ResponseEntity<String> updateVictoryCount(@RequestBody UpdateVictoryCountRequest request) {

        userGradeCommandService.updateVictoryCount(request);

        return ResponseEntity.ok("승리 횟수 수정 완료");
    }


    @DeleteMapping("/{userGradeId}")
    public ResponseEntity<String> deleteUserGrade(@PathVariable Long userGradeId) {

        userGradeCommandService.deleteUserGrade(userGradeId);

        return ResponseEntity.ok("유저 등급 삭제 완료");
    }
}