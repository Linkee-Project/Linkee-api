package com.linkee.linkeeapi.room_user_log.query.controller;

import com.linkee.linkeeapi.common.model.dto.ApiResponse;
import com.linkee.linkeeapi.room_user_log.query.dto.response.RoomUserLogResponse;
import com.linkee.linkeeapi.room_user_log.query.dto.response.RoomUserRankResponse;
import com.linkee.linkeeapi.room_user_log.query.service.RoomUserLogQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * [READ] 퀴즈방 내 사용자 풀이 기록 및 결과 조회 컨트롤러
 * - API 설명:
 *   퀴즈방에서 사용자의 정답 기록과 게임 결과(랭킹, 점수 등)를 조회하는 역할을 한다.
 *   'tb_room_user_log' 테이블을 기반으로 사용자별 기록과 전체 방 결과를 조회한다.
 * - 기본 경로: /api
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RoomUserLogQueryController {

    private final RoomUserLogQueryService roomUserLogQueryService;

    /*
     * [READ-1] 나의 퀴즈 풀이 이력 조회
     * - 요청: GET /api/room_user_log/my_history?roomMemberId={id}
     * - 파라미터: roomMemberId (퀴즈방 참가자 ID)
     * - 반환: List<RoomUserLogResponse>
     * - 기능 설명:
     *   특정 참가자(room_member)가 퀴즈방에서 어떤 문제를 풀었고,
     *   각 문제의 정답 여부(is_corrected)가 무엇인지 조회한다.
     * - 사용 시점:
     *   참가자가 게임이 끝난 후 자신의 풀이 결과를 확인할 때 사용한다.
     */
    @GetMapping("/room_user_log/my_history")
    public ApiResponse<List<RoomUserLogResponse>> getMyAnswerHistory(@RequestParam Long roomMemberId) {
        List<RoomUserLogResponse> response = roomUserLogQueryService.getRoomUserLogs(roomMemberId);
        return ApiResponse.success(response);
    }

    /*
     * [READ-2] 퀴즈방 전체 결과 조회 (랭킹용)
     * - 요청: GET /api/quiz-rooms/{roomId}/result
     * - 경로 변수: roomId (퀴즈방 ID)
     * - 반환: List<RoomUserRankResponse>
     * - 기능 설명:
     *   해당 퀴즈방에 참가한 모든 멤버의 정답 수, 정답률 등을 기반으로
     *   순위를 계산해 반환한다. (랭킹 보드나 결과 요약 화면용)
     * - 사용 시점:
     *   게임 종료 후 결과 페이지를 보여줄 때 사용한다.
     */
    @GetMapping("/quiz_rooms/{roomId}/result")
    public ApiResponse<List<RoomUserRankResponse>> getQuizResult(@PathVariable Long roomId) {
        List<RoomUserRankResponse> response =  roomUserLogQueryService.getQuizResult(roomId);
        return ApiResponse.success(response);
    }

}
