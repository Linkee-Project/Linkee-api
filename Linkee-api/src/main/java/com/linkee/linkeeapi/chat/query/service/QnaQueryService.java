package com.linkee.linkeeapi.chat.query.service;

import com.linkee.linkeeapi.chat.query.dto.response.QnaResponseDto;

import java.util.List;

public interface QnaQueryService {
    List<QnaResponseDto> getQnaListByRoomId(Long roomId);
}
