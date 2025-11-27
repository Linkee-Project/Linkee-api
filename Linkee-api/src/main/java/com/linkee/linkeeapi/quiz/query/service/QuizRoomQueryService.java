package com.linkee.linkeeapi.quiz.query.service;

import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.quiz.query.dto.response.*;

import java.util.List;

public interface QuizRoomQueryService {

    //  방 목록 조회
    PageResponse<QuizRoomListResponseDto> findAllRooms(int page, int size,Long categoryId, String keyword);

    // 빠른 입장
    QuizRoomResponseDto findAvailableRoom();

    // 플레이 상태 조회
    PlayStateResponseDto getPlayState(Long roomId);

    // 결과 조회
    List<ResultRowResponseDto> getResultsByRoom(Long roomId, int page, int size);

    // 방 상세 조회
    QuizRoomDetailResponseDto getRoomDetail(Long roomId, Long currentUserId);
}
