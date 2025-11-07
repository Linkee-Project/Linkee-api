package com.linkee.linkeeapi.quiz_current_index.query.mapper;

import com.linkee.linkeeapi.quiz_current_index.query.dto.response.CurrentQuestionResponse;
import com.linkee.linkeeapi.quiz_current_index.query.dto.response.QuizCurrentIndexResponse;
import com.linkee.linkeeapi.quiz_current_index.query.dto.response.QuizReconnectStateResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface QuizCurrentIndexMapper {

    /*
     * 현재 문제 인덱스를 조회한다.
     * @param roomId 퀴즈방 ID
     * @return 현재 문제 번호 (예: 3)
     */
    int findCurrentIndexByRoomId(@Param("roomId") Long roomId);

    /*
     * 현재 진행 중인 문제의 상세 내용을 조회한다.
     * @param roomId 퀴즈방 ID
     * @return 문제 내용 + 보기 옵션들
     */
    CurrentQuestionResponse findCurrentQuestion(@Param("roomId") Long roomId);

    /*
     * 새로고침 또는 재접속 시 복원을 위한 전체 상태 정보를 조회한다.
     * @param roomId 퀴즈방 ID
     * @return 방 상태 + 현재 문제 번호 + 인원 등
     */
    QuizReconnectStateResponse findReconnectState(@Param("roomId") Long roomId);
}