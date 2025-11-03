package com.linkee.linkeeapi.question.query.controller;

import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.question.query.dto.response.QuestionDetailResponseDto;
import com.linkee.linkeeapi.question.query.dto.response.QuestionListResponseDto;
import com.linkee.linkeeapi.question.query.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/questions")
public class QuestionController {

    private final QuestionService questionService;

    //문제 목록 조회 + 옵션(keyword)
    @GetMapping
    public ResponseEntity<PageResponse<QuestionListResponseDto>> getQuestionsList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String keyword
    ) {
        PageResponse< QuestionListResponseDto> response =
                questionService.getQuestionList(page,size,keyword);
        return ResponseEntity.ok(response);
    }

    //문제 카테고리별 조회 + 옵션(keyword)
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<PageResponse<QuestionListResponseDto>> getQuestionsListByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String keyword
    ) {
        PageResponse<QuestionListResponseDto> response =
                questionService.getQuestionsListByCategory(page, size, categoryId,keyword);
        return ResponseEntity.ok(response);
    }

    //문제 상세 조회
    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionDetailResponseDto> getQuestionDetail(@PathVariable Long questionId){
        QuestionDetailResponseDto detailQuestion = questionService.getQuestionDetail(questionId);

        return ResponseEntity.ok(detailQuestion);
    }

}
