package com.linkee.linkeeapi.quiz_room.query.service;

import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.quiz_room.query.mapper.QuizRoomMapper;
import com.linkee.linkeeapi.quiz_room.query.dto.response.PlayStateResponseDto;
import com.linkee.linkeeapi.quiz_room.query.dto.response.QuizRoomListResponseDto;
import com.linkee.linkeeapi.quiz_room.query.dto.response.QuizRoomResponseDto;
import com.linkee.linkeeapi.quiz_room.query.dto.response.ResultRowResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizRoomQueryServiceImpl implements QuizRoomQueryService {

    private final QuizRoomMapper quizRoomMapper;

    //  방 목록 조회
    @Override
    public PageResponse<QuizRoomListResponseDto> findAllRooms(int page, int size) {
        int offset = page * size;
        List<QuizRoomListResponseDto> rooms = quizRoomMapper.findAllRoomsPaginated(size, offset);
        int total = quizRoomMapper.countAllRooms();
        return PageResponse.from(rooms, offset, size, total);
    }

    //  빠른 입장
    @Override
    public QuizRoomResponseDto findAvailableRoom() {
        return quizRoomMapper.findAvailableRoom();
    }

    //  플레이 상태
    @Override
    public PlayStateResponseDto getPlayState(Long roomId) {
        return quizRoomMapper.findPlayState(roomId);
    }

    //  결과 조회
    @Override
    public List<ResultRowResponseDto> getResultsByRoom(Long roomId, int page, int size) {
        int offset = page * size;
        return quizRoomMapper.findResultsByRoom(roomId, size, offset);
    }
}
