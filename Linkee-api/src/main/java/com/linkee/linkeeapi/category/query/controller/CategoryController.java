package com.linkee.linkeeapi.category.query.controller;

import com.linkee.linkeeapi.category.query.dto.response.CategoryResponse;
import com.linkee.linkeeapi.category.query.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
@Tag(name = "퀴즈", description = "퀴즈방 생성, 입장, 진행 관련 API")
public class CategoryController {

    private final CategoryService categoryService;

    // 드롭다운/필터용 전체 카테고리
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> list() {
        return ResponseEntity.ok(categoryService.getAll());
    }
}