package com.linkee.linkeeapi.room_member.command.application.controller;

import com.linkee.linkeeapi.common.model.dto.ApiResponse;
import com.linkee.linkeeapi.room_member.command.application.dto.request.RoomMemberCreateRequest;
import com.linkee.linkeeapi.room_member.command.application.dto.response.RoomMemberCreateResponse;
import com.linkee.linkeeapi.room_member.command.application.service.RoomMemberCommandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/*
 * 룸 멤버 관련 CUD(Create, Update, Delete) 작업을 처리하는 컨트롤러.
 * API 요청을 받아 RoomMemberCommandService로 전달하고, 결과를 반환합니다.
 */
@RestController
@RequestMapping("/api/v1/room_members")
@RequiredArgsConstructor
@Tag(name = "퀴즈", description = "퀴즈방 생성, 입장, 진행 관련 API")
public class RoomMemberCommandController {

    private final RoomMemberCommandService roomMemberCommandService;

    /*
     * 새로운 룸 멤버를 생성합니다.
     * @param request 생성할 룸 멤버의 정보를 담은 요청 객체
     * @return 성공 응답
     */
    @PostMapping
    public ApiResponse<RoomMemberCreateResponse> createRoomMember(@RequestBody RoomMemberCreateRequest request) {
        RoomMemberCreateResponse response = roomMemberCommandService.createRoomMember(request);
        return ApiResponse.success(response);
    }

    /*
     * 특정 룸 멤버의 준비 상태를 토글합니다. (Y -> N, N -> Y)
     * @param roomMemberId 준비 상태를 변경할 룸 멤버의 ID
     * @return 성공 응답
     */
    @PatchMapping("/{roomMemberId}/ready")
    public ApiResponse<Void> toggleReady(@PathVariable Long roomMemberId) {
        roomMemberCommandService.toggleReady(roomMemberId);
        return ApiResponse.success(null);
    }

    /*
     * 룸 멤버가 스스로 방을 나갑니다. (자발적 나감)
     * @param roomMemberId 나가는 룸 멤버의 ID
     * @return 성공 응답
     */
    @PatchMapping("/{roomMemberId}/selfLeave")
    public ApiResponse<Void> selfLeaveRoom(@PathVariable Long roomMemberId) {
        roomMemberCommandService.selfLeaveRoom(roomMemberId);
        return ApiResponse.success(null);
    }

    /*
     * 방장이 룸 멤버를 강제로 내보냅니다. (강퇴)
     * @param roomMemberId 강퇴할 룸 멤버의 ID
     * @return 성공 응답
     */
    @PatchMapping("/{roomMemberId}/kick")
    public ApiResponse<Void> kickRoomMember(@PathVariable Long roomMemberId) {
        roomMemberCommandService.kickRoomMember(roomMemberId);
        return ApiResponse.success(null);
    }
}

