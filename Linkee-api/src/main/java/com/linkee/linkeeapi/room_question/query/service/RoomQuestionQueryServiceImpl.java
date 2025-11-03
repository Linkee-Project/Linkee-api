package com.linkee.linkeeapi.room_question.query.service;

import com.linkee.linkeeapi.room_question.query.dto.response.RoomQuestionResponse;
import com.linkee.linkeeapi.room_question.query.mapper.RoomQuestionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//  문제 목록 조회
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomQuestionQueryServiceImpl implements RoomQuestionQueryService {

    private final RoomQuestionMapper roomQuestionMapper;

    @Override
    public List<RoomQuestionResponse> getRoomQuestionsByRoomId(Long roomId) {
        return roomQuestionMapper.findByRoomId(roomId);
    }
}