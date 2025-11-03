package com.linkee.linkeeapi.qna.query.mapper;

import com.linkee.linkeeapi.qna.query.dto.response.QnaResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QnaMapper {

    List<QnaResponseDto> findAllByRoomId(@Param("roomId") Long roomId);
}
