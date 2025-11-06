package com.linkee.linkeeapi.qna.command.application.controller;

import com.linkee.linkeeapi.qna.command.application.dto.request.CreateQnaRequestDto;
import com.linkee.linkeeapi.qna.command.application.service.QnaCommandService;
import com.linkee.linkeeapi.qna.command.domain.aggregate.Qna;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/qna")
@Tag(name = "자율", description = "자율방(자유 출제방) 관련 API")
public class QnaCommandController {

    private final QnaCommandService qnaCommandService;

    @PostMapping
    public ResponseEntity<String> createQna(@RequestBody CreateQnaRequestDto request) {
        qnaCommandService.createQna(request);
        return ResponseEntity.ok("문제가 성공적으로 등록 했습니다 ");
    }
}
