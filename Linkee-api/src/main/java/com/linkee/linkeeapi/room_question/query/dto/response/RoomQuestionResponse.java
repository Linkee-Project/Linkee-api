package com.linkee.linkeeapi.room_question.query.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RoomQuestionResponse {
    private Long roomQuestionId;    //  퀴즈방과 문제의 연결고리 ID
    private Long questionId;        //  실제 문제의 고유 ID
    private String questionContent; //  문제의 실제 내용 텍스트
    private List<String> options;   // 문제에 대한 객관식 보기들의 리스트

    @Builder
    public RoomQuestionResponse(Long roomQuestionId, Long questionId, String questionContent, List<String> options) {
        this.roomQuestionId = roomQuestionId;
        this.questionId = questionId;
        this.questionContent = questionContent;
        this.options = options;
    }
}
