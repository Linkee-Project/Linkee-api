package com.linkee.linkeeapi.room_question.query.controller;


import com.linkee.linkeeapi.common.model.dto.ApiResponse;
import com.linkee.linkeeapi.room_question.query.service.RoomQuestionQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/quiz_rooms/{roomId}/questions")
@Tag(name = "퀴즈", description = "퀴즈방 생성, 입장, 진행 관련 API")
public class RoomQuestionQueryController {

    private final RoomQuestionQueryService roomQuestionQueryService;

    @GetMapping
    public ApiResponse<?> getRoomQuestions(@PathVariable Long roomId) {

        return ApiResponse.success(roomQuestionQueryService.getRoomQuestionsByRoomId(roomId));
    }
    /*
     * [READ] 특정 퀴즈방의 문제 세트 조회 API
     *
     * - 엔드포인트: GET /api/v1/quiz_rooms/{roomId}/questions
     * - 설명:
     *   퀴즈방(게임방)에 출제된 모든 문제를 순서대로 조회한다.
     *   → 각 퀴즈방 생성 시, 검증된 문제 중 랜덤으로 배정된 문제들이 있다.
     *   → 이 API는 그 문제 목록(제목, 보기 등)을 반환한다.
     *
     * - 사용 시점:
     *   1) 게임 대기 화면에서 “이번 퀴즈방에 어떤 문제가 나오는지” 미리 조회할 때
     *   2) 게임 시작 시 클라이언트가 문제 순서대로 보여줄 데이터를 미리 가져올 때
     *
     * - 반환 데이터:
     *   List<RoomQuestionResponse>
     *     ├─ roomQuestionId  (퀴즈방 문제 고유 ID)
     *     ├─ quizRoomId      (해당 퀴즈방 ID)
     *     ├─ questionId      (문제 ID)
     *     ├─ quizOrder       (출제 순서)
     *     ├─ questionTitle   (문제 제목)
     *     └─ choice1~4       (객관식 보기)
     */

}
