package com.linkee.linkeeapi.room_member.query.service;

import com.linkee.linkeeapi.room_member.query.dto.request.RoomMemberSearchRequest;
import com.linkee.linkeeapi.room_member.query.dto.response.RoomMemberResponse;
import com.linkee.linkeeapi.room_member.query.mapper.RoomMemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomMemberQueryServiceImpl implements RoomMemberQueryService {

    private final RoomMemberMapper roomMemberMapper;

    @Override
    public List<RoomMemberResponse> selectAllRoomMember(RoomMemberSearchRequest request) {
        // Mapper를 통해 데이터베이스에서 멤버 목록을 바로 조회하여 반환
        return roomMemberMapper.selectAllRoomMember(request);
    }
}
