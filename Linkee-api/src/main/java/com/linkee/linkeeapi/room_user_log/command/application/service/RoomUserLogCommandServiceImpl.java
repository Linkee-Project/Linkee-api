package com.linkee.linkeeapi.room_user_log.command.application.service;

import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.room_member.command.infrastructure.repository.RoomMemberRepository;
import com.linkee.linkeeapi.room_question.command.infrastructure.repository.RoomQuestionRepository;
import com.linkee.linkeeapi.room_user_log.command.application.dto.request.RoomUserLogCreateRequestDto;
import com.linkee.linkeeapi.room_user_log.command.domain.aggregate.RoomUserLog;
import com.linkee.linkeeapi.room_user_log.command.infrastructure.repository.JpaRoomUserLogRepository;
import com.linkee.linkeeapi.room_question.command.domain.aggregate.RoomQuestion;
import com.linkee.linkeeapi.room_member.command.domain.aggregate.RoomMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/* 퀴즈방 사용자 풀이 기록(정답 여부 등)을 DB에 저장하는 Command 서비스 구현체 */
@Service
@RequiredArgsConstructor
public class RoomUserLogCommandServiceImpl implements RoomUserLogCommandService {
    // JPA 기반 저장소 (tb_room_user_log 테이블에 데이터 INSERT)
    private final JpaRoomUserLogRepository jpaRoomUserLogRepository;
    // 출제된 문제(room_question) 조회용 Repository
    private final RoomQuestionRepository roomQuestionRepository;
    // 퀴즈방 참여자(room_member) 조회용 Repository
    private final RoomMemberRepository roomMemberRepository;

    @Override
    @Transactional
    public Long createRoomUserLog(RoomUserLogCreateRequestDto requestDto) {
        // room_question_id 로 문제 존재 여부 확인
        RoomQuestion roomQuestion = roomQuestionRepository.findById(requestDto.getRoomQuestionId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_QUESTION_NOT_FOUND));
        // room_member_id 로 퀴즈방 내 사용자 존재 여부 확인
        RoomMember roomMember = roomMemberRepository.findById(requestDto.getRoomMemberId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_MEMBER_NOT_FOUND));
        // RoomUserLog 엔티티 생성 (문제, 사용자, 정답 여부 매핑)
        RoomUserLog roomUserLog = RoomUserLog.builder()
                .roomQuestion(roomQuestion)
                .roomMember(roomMember)
                .isCorrected(requestDto.getIsCorrected())
                .build();
        // DB 저장 및 생성된 PK 반환
        return jpaRoomUserLogRepository.save(roomUserLog).getRoomUserLogId();
    }
}
