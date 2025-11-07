package com.linkee.linkeeapi.user_grade.query.controller;

import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.user_grade.query.dto.request.UserGradeSearchRequest;
import com.linkee.linkeeapi.user_grade.query.dto.response.UserGradeResponse;
import com.linkee.linkeeapi.user_grade.query.service.UserGradeQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user_grades")
@Tag(name = "회원", description = "회원가입, 로그인, 계정, 프로필 관련 API")
public class UserGradeQueryController {


    private final UserGradeQueryService userGradeQueryService;

    @GetMapping
    public ResponseEntity<PageResponse<UserGradeResponse>> getAllUserGrades(UserGradeSearchRequest request) {
        return ResponseEntity.ok(userGradeQueryService.selectAllUserGrades(request));
    }
}
