package com.linkee.linkeeapi.quiz_room.command.application.service;

import com.linkee.linkeeapi.quiz_room.command.application.dto.request.QuizRoomCreateRequestDto;
import com.linkee.linkeeapi.quiz_room.command.application.dto.request.QuizRoomDeleteRequestDto;

/*
 * 퀴즈룸 생성 관련 비즈니스 로직을 정의하는 서비스 인터페이스.
 * CQRS 패턴의 Command 측면에서 퀴즈룸의 상태를 변경하는 작업을 담당합니다.
 */
public interface QuizRoomCommandService {
    /*
     * 새로운 퀴즈룸을 생성하는 메서드.
     * @param request 퀴즈룸 생성에 필요한 데이터를 담은 DTO
     * @param userId 퀴즈룸을 생성하는 사용자의 ID (방장)
     * @return 생성된 퀴즈룸의 고유 ID
     */
    Long create(QuizRoomCreateRequestDto request, Long userId);

    void deleteQuizRoom(QuizRoomDeleteRequestDto request);

    void leaveQuizRoom(QuizRoomDeleteRequestDto request);
}
