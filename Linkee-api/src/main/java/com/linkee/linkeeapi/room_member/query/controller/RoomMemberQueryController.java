package com.linkee.linkeeapi.room_member.query.controller;

import com.linkee.linkeeapi.room_member.query.dto.request.RoomMemberSearchRequest;
import com.linkee.linkeeapi.room_member.query.dto.response.RoomMemberResponse;
import com.linkee.linkeeapi.room_member.query.service.RoomMemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/room_members")
public class RoomMemberQueryController {

    private final RoomMemberQueryService roomMemberQueryService;

    /*
     * [API] GET /api/v1/room_members
     * [설명] 특정 퀴즈방의 멤버 목록 전체를 조회하여 반환합니다.
     *       (예: /api/v1/room_members?quizRoomId=1)
     */
    @GetMapping
    public List<RoomMemberResponse> selectAllRoomMember(RoomMemberSearchRequest request) {
        return roomMemberQueryService.selectAllRoomMember(request);
    }
}
