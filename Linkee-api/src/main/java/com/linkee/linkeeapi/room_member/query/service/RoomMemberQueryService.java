package com.linkee.linkeeapi.room_member.query.service;

import com.linkee.linkeeapi.room_member.query.dto.request.RoomMemberSearchRequest;
import com.linkee.linkeeapi.room_member.query.dto.response.RoomMemberResponse;
import java.util.List;

public interface RoomMemberQueryService {
    /*
     * 퀴즈방 멤버 목록 전체를 조회합니다.
     * @param request 검색 조건
     * @return 멤버 목록
     */
    List<RoomMemberResponse> selectAllRoomMember(RoomMemberSearchRequest request);
}
