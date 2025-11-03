package com.linkee.linkeeapi.room_question.query.mapper;

import com.linkee.linkeeapi.room_question.query.dto.response.RoomQuestionResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoomQuestionMapper {
    List<RoomQuestionResponse> findByRoomId(@Param("roomId") Long roomId);
}