package com.linkee.linkeeapi.question.query.controller;

import com.linkee.linkeeapi.question.query.dto.response.CategoryResponseDto;
import com.linkee.linkeeapi.question.query.service.CategoryQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/question/categories")
@Tag(name = "퀴즈", description = "퀴즈방 생성, 입장, 진행 관련 API")
public class CategoryController {

    private final CategoryQueryService categoryService;

    // 드롭다운/필터용 전체 카테고리
    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> list() {
        return ResponseEntity.ok(categoryService.getAll());
    }
}