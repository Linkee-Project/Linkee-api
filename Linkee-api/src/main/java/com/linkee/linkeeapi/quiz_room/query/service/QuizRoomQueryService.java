package com.linkee.linkeeapi.quiz_room.query.service;

import com.linkee.linkeeapi.quiz_room.query.dto.response.PlayStateResponseDto;
import com.linkee.linkeeapi.quiz_room.query.dto.response.QuizRoomListResponseDto;
import com.linkee.linkeeapi.quiz_room.query.dto.response.QuizRoomResponseDto;
import com.linkee.linkeeapi.quiz_room.query.dto.response.ResultRowResponseDto;

import java.util.List;

public interface QuizRoomQueryService {

    //  방 목록 조회
    List<QuizRoomListResponseDto> findAllRooms(int page, int size);

    // 빠른 입장
    QuizRoomResponseDto findAvailableRoom();

    // 플레이 상태 조회
    PlayStateResponseDto getPlayState(Long roomId);

    // 결과 조회
    List<ResultRowResponseDto> getResultsByRoom(Long roomId, int page, int size);
}
