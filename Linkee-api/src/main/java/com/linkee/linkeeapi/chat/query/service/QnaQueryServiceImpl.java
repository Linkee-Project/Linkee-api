package com.linkee.linkeeapi.chat.query.service;

import com.linkee.linkeeapi.chat.query.dto.response.QnaResponseDto;
import com.linkee.linkeeapi.chat.query.mapper.QnaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QnaQueryServiceImpl implements QnaQueryService{

    private final QnaMapper qnaMapper;
    @Override
    public List<QnaResponseDto> getQnaListByRoomId(Long roomId) {
        return qnaMapper.findAllByRoomId(roomId);
    }
}
