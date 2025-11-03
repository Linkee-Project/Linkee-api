package com.linkee.linkeeapi.room_question.command.application.service;

import com.linkee.linkeeapi.question.command.infrastructure.repository.JpaQuestionRepository;
import com.linkee.linkeeapi.quiz_room.command.infrastructure.repository.QuizRoomRepository;
import com.linkee.linkeeapi.room_question.command.application.dto.request.RoomQuestionCreateRequest;
import com.linkee.linkeeapi.room_question.command.domain.aggregate.RoomQuestion;
import com.linkee.linkeeapi.room_question.command.infrastructure.repository.RoomQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomQuestionCommandServiceImpl implements RoomQuestionCommandService {

    private final QuizRoomRepository quizRoomRepository;
    private final JpaQuestionRepository jpaQuestionRepository;
    private final RoomQuestionRepository roomQuestionRepository;

    @Override
    @Transactional
    public Long createRoomQuestion(RoomQuestionCreateRequest request) {
        var quizRoom = quizRoomRepository.findById(request.getQuizRoomId())
                .orElseThrow(() -> new IllegalArgumentException("QuizRoom not found with id: " + request.getQuizRoomId()));
        var question = jpaQuestionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new IllegalArgumentException("Question not found with id: " + request.getQuestionId()));

        RoomQuestion roomQuestion = RoomQuestion.builder()
                .quizRoom(quizRoom)
                .question(question)
                .quizOrder(request.getQuizOrder())
                .build();

        roomQuestionRepository.save(roomQuestion);
        return roomQuestion.getRoomQuestionId();
    }
    /*
     * [CREATE] 퀴즈방 문제 생성 기능
     *
     * - 목적:
     *   특정 퀴즈방(게임방)에 문제를 등록한다.
     *   즉, "어떤 방에 어떤 문제가 몇 번째 순서로 출제될지"를 저장하는 기능이다.
     *
     * - 동작 흐름:
     *   1. 요청으로 전달된 quizRoomId, questionId, quizOrder 를 바탕으로
     *      퀴즈방과 문제 엔티티를 각각 DB에서 조회한다.
     *      - 퀴즈방이 존재하지 않으면 IllegalArgumentException 발생
     *      - 문제(question)가 존재하지 않으면 IllegalArgumentException 발생
     *
     *   2. 조회된 퀴즈방과 문제를 이용해 RoomQuestion 엔티티를 생성한다.
     *      - quizRoom : 출제 대상이 되는 게임방
     *      - question : 실제 문제 데이터
     *      - quizOrder : 출제 순서(1번 문제, 2번 문제, …)
     *
     *   3. 생성된 RoomQuestion 객체를 DB에 저장(roomQuestionRepository.save).
     *
     *   4. 저장된 엔티티의 roomQuestionId(퀴즈방 문제 ID)를 반환한다.
     *
     * - 사용 시점:
     *   - 퀴즈방 생성 직후, 검증된 문제들을 랜덤으로 매핑할 때
     *   - 관리자나 시스템이 직접 문제 세트를 구성할 때
     *
     * - 주요 예외:
     *   - IllegalArgumentException("QuizRoom not found with id: ...")
     *   - IllegalArgumentException("Question not found with id: ...")
     *
     * - 반환값:
     *   생성된 RoomQuestion 엔티티의 고유 ID(Long)
     */

}
