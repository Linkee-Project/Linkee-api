package com.linkee.linkeeapi.quiz.query.mapper;
import com.linkee.linkeeapi.quiz.query.dto.response.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface QuizRoomMapper {

    //  방 목록 조회 (페이징)
    List<QuizRoomListResponseDto> findAllRoomsPaginated(@Param("limit") int limit,
                                                        @Param("offset") int offset,
                                                        @Param("categoryId") Long categoryId,
                                                        @Param("keyword") String keyword );
    // 필터링 된 방 개수 조회
    int countFilteredRooms(
            @Param("categoryId") Long categoryId,
            @Param("keyword") String keyword
    );

    //  빠른 시작
    QuizRoomResponseDto findAvailableRoom();

    // 플레이 상태 조회
    PlayStateResponseDto findPlayState(Long roomId);

    // 결과 조회
    List<ResultRowResponseDto> findResultsByRoom(@Param("roomId") Long roomId, @Param("limit") int limit, @Param("offset") int offset);

    // 방 헤더(제목/방장)만 조회 — 멤버는 RoomMemberMapper 재사용
    RoomHeaderRow findRoomHeader(@Param("roomId") Long roomId);
}
