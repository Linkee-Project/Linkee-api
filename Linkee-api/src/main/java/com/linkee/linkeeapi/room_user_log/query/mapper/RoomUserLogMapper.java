package com.linkee.linkeeapi.room_user_log.query.mapper;

import com.linkee.linkeeapi.room_user_log.query.dto.response.RoomUserLogResponse;
import com.linkee.linkeeapi.room_user_log.query.dto.response.RoomUserRankResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoomUserLogMapper {
    List<RoomUserLogResponse> findByRoomMemberId(Long roomMemberId);
    List<RoomUserRankResponse> findRankByRoomId(Long roomId);
}
