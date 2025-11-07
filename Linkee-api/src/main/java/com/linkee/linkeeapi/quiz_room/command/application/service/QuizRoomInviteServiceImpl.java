package com.linkee.linkeeapi.quiz_room.command.application.service;

import com.linkee.linkeeapi.common.event.QuizRoomInviteEvent;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.quiz_room.command.application.dto.request.QuizRoomInviteRequestDto;
import com.linkee.linkeeapi.quiz_room.command.domain.aggregate.QuizRoom;
import com.linkee.linkeeapi.quiz_room.command.infrastructure.repository.QuizRoomRepository;
import com.linkee.linkeeapi.relation.command.domain.aggregate.entity.Relation;
import com.linkee.linkeeapi.relation.command.domain.aggregate.entity.RelationStatus;
import com.linkee.linkeeapi.relation.command.infrastructure.repository.RelationRepository;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user.command.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizRoomInviteServiceImpl implements QuizRoomInviteService {

    private final UserRepository userRepository;
    private final QuizRoomRepository quizRoomRepository;
    private final RelationRepository relationRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void sendInvite(QuizRoomInviteRequestDto request) {
        // 1. 초대하는 유저, 초대받는 유저, 퀴즈방 정보 조회
        User inviter = userRepository.findById(request.getInviterId())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_USER_ID));
        User invitee = userRepository.findById(request.getInviteeId())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_USER_ID));
        QuizRoom quizRoom = quizRoomRepository.findById(request.getQuizRoomId())
                .orElseThrow(() -> new BusinessException(ErrorCode.QUIZ_ROOM_NOT_FOUND));

        // 2. (안전장치) 친구 관계인지 확인 (초대하는 순간에 친구 관계가 끊겼을 경우 등 대비)
        Relation relation = relationRepository.findByUsers(inviter, invitee)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REQUEST,"친구 관계가 아닙니다."));
        if (relation.getRelationStatus() != RelationStatus.A) { // 승인 상태가 아닐 시
            throw new BusinessException(ErrorCode.INVALID_REQUEST,"친구 관계가 아닙니다.");
        }

        // 3. 초대 이벤트 발생
        eventPublisher.publishEvent(new QuizRoomInviteEvent(
                this,
                invitee.getUserId(),
                inviter.getUserNickname(),
                quizRoom.getRoomTitle()
        ));
    }
}
