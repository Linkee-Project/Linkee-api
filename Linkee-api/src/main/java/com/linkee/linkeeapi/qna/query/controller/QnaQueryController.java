package com.linkee.linkeeapi.qna.query.controller;

import com.linkee.linkeeapi.qna.query.dto.response.QnaResponseDto;
import com.linkee.linkeeapi.qna.query.service.QnaQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/qna")
@Tag(name = "자율", description = "자율방(자유 출제방) 관련 API")
public class QnaQueryController {

    private final QnaQueryService qnaQueryService;

    @GetMapping("/{roomId}")
    public List<QnaResponseDto> getQnaList(@PathVariable Long roomId) {
        return qnaQueryService.getQnaListByRoomId(roomId);
    }
}
