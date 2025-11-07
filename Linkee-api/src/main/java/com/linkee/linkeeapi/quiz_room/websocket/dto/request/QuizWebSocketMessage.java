package com.linkee.linkeeapi.quiz_room.websocket.dto.request;

import com.linkee.linkeeapi.quiz_room.websocket.dto.QuizMessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizWebSocketMessage {
    private QuizMessageType type;
    private Long roomId;
    private Long userId;
    private String userName;           // 화면에 "홍길동님이 제출했습니다" 표시용
    private Long questionId;
    private Long selectedOptionId;
}
