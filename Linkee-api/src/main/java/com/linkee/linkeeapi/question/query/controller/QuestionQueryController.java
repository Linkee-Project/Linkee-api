package com.linkee.linkeeapi.question.query.controller;

import com.linkee.linkeeapi.common.model.CustomUser; // Import CustomUser
import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.question.query.dto.response.QuestionDetailResponseDto;
import com.linkee.linkeeapi.question.query.dto.response.QuestionListResponseDto;
import com.linkee.linkeeapi.question.query.service.QuestionQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // Import AuthenticationPrincipal
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/question/questions")
@Tag(name = "문제", description = "문제 게시판 및 댓글 관리 API")
public class QuestionQueryController {

    private final QuestionQueryService questionService;

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

    // 현재 로그인한 유저가 작성한 문제 리스트 조회 + 옵션(keyword)
    @GetMapping("/my-questions") // Changed endpoint path
    public ResponseEntity<PageResponse<QuestionListResponseDto>> getQuestionsForCurrentUser( // Changed method name
            @AuthenticationPrincipal CustomUser customUser, // Get userId from authenticated user
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String keyword
    ) {
        PageResponse<QuestionListResponseDto> response =
                questionService.getQuestionsByCurrentUser(customUser.getUserId(), page, size, keyword); // Call new service method
        return ResponseEntity.ok(response);
    }

    //문제 상세 조회
    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionDetailResponseDto> getQuestionDetail(@PathVariable Long questionId){
        QuestionDetailResponseDto detailQuestion = questionService.getQuestionDetail(questionId);

        return ResponseEntity.ok(detailQuestion);
    }

}
