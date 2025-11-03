package com.linkee.linkeeapi.qna.query.service;

import com.linkee.linkeeapi.qna.query.dto.response.QnaResponseDto;

import java.util.List;

public interface QnaQueryService {
    List<QnaResponseDto> getQnaListByRoomId(Long roomId);
}
