package com.linkee.linkeeapi.qna.query.service;

import com.linkee.linkeeapi.qna.query.dto.response.QnaResponseDto;
import com.linkee.linkeeapi.qna.query.mapper.QnaMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QnaQueryServiceImplTest {

    @Mock
    private QnaMapper qnaMapper;

    @InjectMocks
    private QnaQueryServiceImpl qnaQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("채팅방 ID로 Qna 목록 조회 성공")
    void testGetQnaListByRoomId() {
        Long roomId = 1L;

        QnaResponseDto qna1 = QnaResponseDto.builder()
                .qnaQuestion("질문1")
                .qnaAnswer("답1")
                .build();

        QnaResponseDto qna2 = QnaResponseDto.builder()
                .qnaQuestion("질문2")
                .qnaAnswer("답2")
                .build();

        List<QnaResponseDto> qnaList = Arrays.asList(qna1, qna2);

        when(qnaMapper.findAllByRoomId(roomId)).thenReturn(qnaList);

        List<QnaResponseDto> result = qnaQueryService.getQnaListByRoomId(roomId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("질문1", result.get(0).getQnaQuestion());
        assertEquals("답2", result.get(1).getQnaAnswer());

        verify(qnaMapper, times(1)).findAllByRoomId(roomId);
    }


}
