package com.linkee.linkeeapi.grade.query.controller;

import com.linkee.linkeeapi.grade.query.dto.request.GradeSearchRequest;
import com.linkee.linkeeapi.grade.query.dto.response.GradeResponse;
import com.linkee.linkeeapi.grade.query.service.GradeQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/grades")
@RequiredArgsConstructor
public class GradeQueryController {

    private final GradeQueryService gradeQueryService;

    @GetMapping
    public ResponseEntity<List<GradeResponse>> getAllGrades(GradeSearchRequest request) {
        return ResponseEntity.ok(gradeQueryService.selectAllGrade(request));
    }

}
