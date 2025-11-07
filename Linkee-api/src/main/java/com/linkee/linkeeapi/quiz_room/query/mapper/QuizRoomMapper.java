package com.linkee.linkeeapi.quiz_room.query.mapper;
import com.linkee.linkeeapi.quiz_room.query.dto.response.PlayStateResponseDto;
import com.linkee.linkeeapi.quiz_room.query.dto.response.ResultRowResponseDto;
import com.linkee.linkeeapi.quiz_room.query.dto.response.QuizRoomListResponseDto;
import com.linkee.linkeeapi.quiz_room.query.dto.response.QuizRoomResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface QuizRoomMapper {

    //  방 목록 조회 (페이징)
    List<QuizRoomListResponseDto> findAllRoomsPaginated(@Param("limit") int limit, @Param("offset") int offset);

    // 전체 방 개수 조회
    int countAllRooms();

    //  빠른 시작
    QuizRoomResponseDto findAvailableRoom();

    // 플레이 상태 조회
    PlayStateResponseDto findPlayState(Long roomId);

    // 결과 조회
    List<ResultRowResponseDto> findResultsByRoom(@Param("roomId") Long roomId, @Param("limit") int limit, @Param("offset") int offset);
}
