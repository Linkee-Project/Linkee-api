package com.linkee.linkeeapi.quiz.query.mapper;

import com.linkee.linkeeapi.quiz.query.dto.response.RoomQuestionResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoomQuestionMapper {
    List<RoomQuestionResponse> findByRoomId(@Param("roomId") Long roomId);
}