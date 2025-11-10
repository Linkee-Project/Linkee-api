package com.linkee.linkeeapi.quiz.query.service;

import com.linkee.linkeeapi.quiz.query.dto.response.RoomUserLogResponse;
import com.linkee.linkeeapi.quiz.query.dto.response.RoomUserRankResponse;
import com.linkee.linkeeapi.quiz.query.mapper.RoomUserLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomUserLogQueryServiceImpl implements RoomUserLogQueryService {
    private final RoomUserLogMapper roomUserLogMapper;

    @Override
    public List<RoomUserLogResponse> getRoomUserLogs(Long roomMemberId) {
        return roomUserLogMapper.findByRoomMemberId(roomMemberId);
    }

    @Override
    public List<RoomUserRankResponse> getQuizResult(Long roomId) {
        return roomUserLogMapper.findRankByRoomId(roomId);
    }


}
