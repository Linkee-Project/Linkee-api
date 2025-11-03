package com.linkee.linkeeapi.room_member.query.mapper;

import com.linkee.linkeeapi.room_member.query.dto.request.RoomMemberSearchRequest;
import com.linkee.linkeeapi.room_member.query.dto.response.RoomMemberResponse;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface RoomMemberMapper {
    /*
     * 조건에 맞는 퀴즈방 멤버 목록 전체를 조회합니다.
     * @param request 검색 조건 (퀴즈방 ID)
     * @return 퀴즈방 멤버 목록
     */
    List<RoomMemberResponse> selectAllRoomMember(RoomMemberSearchRequest request);
}
