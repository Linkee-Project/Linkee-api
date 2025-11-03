package com.linkee.linkeeapi.grade.command.application.controller;

import com.linkee.linkeeapi.grade.command.application.dto.request.UpdateGradeNameRequest;
import com.linkee.linkeeapi.grade.command.application.service.GradeCommandService;
import com.linkee.linkeeapi.grade.command.domain.aggregate.entity.Grade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/grades")
public class GradeCommandController {

    private final GradeCommandService gradeCommandService;


    @PostMapping
    public ResponseEntity<Grade> createGrade(@RequestParam String gradeName) {
        return ResponseEntity.ok(gradeCommandService.createGrade(gradeName));
    }


    @PatchMapping()
    public ResponseEntity<String> updateGradeName(@RequestBody UpdateGradeNameRequest request) {
        gradeCommandService.updateGradeName(request);
        return ResponseEntity.ok("등급 이름 변경 완료");
    }


    @DeleteMapping("/{gradeId}")
    public ResponseEntity<String> deleteGrade(@PathVariable Long gradeId) {
        gradeCommandService.deleteGrade(gradeId);
        return ResponseEntity.ok("등급 삭제 완료");
    }
}
