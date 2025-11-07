package com.linkee.linkeeapi.relation.command.application.service;

import com.linkee.linkeeapi.alarm_box.command.domain.aggregate.entity.AlarmBox;
import com.linkee.linkeeapi.alarm_box.command.infrastructure.repository.AlarmBoxRepository;
import com.linkee.linkeeapi.alarm_template.query.dto.response.AlarmTemplateResponse;
import com.linkee.linkeeapi.alarm_template.query.mapper.AlarmTemplateMapper;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.event.FriendRequestEvent;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.relation.command.application.dto.request.RelationCreateRequest;
import com.linkee.linkeeapi.relation.command.domain.aggregate.entity.Relation;
import com.linkee.linkeeapi.relation.command.domain.aggregate.entity.RelationStatus;
import com.linkee.linkeeapi.relation.command.infrastructure.repository.RelationRepository;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user.command.infrastructure.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class RelationCommandService {

    private final RelationRepository relationRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final AlarmBoxRepository alarmBoxRepository;
    private final AlarmTemplateMapper alarmTemplateMapper;

    @Transactional
    public void createRelation(RelationCreateRequest request) {

        User requester = userRepository.findById(request.getRequesterId())
                .orElseThrow(() -> new IllegalArgumentException("잘못 된 사용자입니다"));
        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new IllegalArgumentException("잘못 된 사용자입니다"));

        // 두 사용자 간의 관계가 이미 존재하는지 확인
        Optional<Relation> optionalRelation = relationRepository.findByUsers(requester, receiver);

        if (optionalRelation.isPresent()) {
            // 관계가 이미 존재하면 , 재활용
            Relation existingRelation = optionalRelation.get();

            switch (existingRelation.getRelationStatus()) {
                case A:
                    throw new BusinessException(ErrorCode.RELATION_ALREADY_EXISTS);
                case P:
                    throw new BusinessException(ErrorCode.RELATION_REQUEST_ALREADY_EXISTS);
                case R:
                    // 거절된 상태에서 다시 요청하는 경우, 기존 데이터를 업데이트
                    existingRelation.reRequest(requester, receiver);
                    relationRepository.save(existingRelation);
                    break;
            }
        } else {
            Relation relation = Relation.builder()
                    .requester(requester)
                    .receiver(receiver)
                    .build();

            relationRepository.save(relation);
        }
        // sse
        eventPublisher.publishEvent(new FriendRequestEvent(this, requester, receiver));
    }


    @Transactional
    public void updateRelationStatus(Long relationId, RelationStatus status) {
        Relation relation = relationRepository.findById(relationId)
                .orElseThrow(() -> new IllegalArgumentException("잘못 된 사용자입니다"));

        relation.modifyRelationStatus(status);

        // 친구 요청을 수락하거나 거절 하면 알림 업데이트
        if (status == RelationStatus.A || status == RelationStatus.R) {
            User receiver = relation.getReceiver();
            AlarmTemplateResponse alarmTemplate = alarmTemplateMapper.selectAlarmTemplateById(1L);
            String alarmContent = String.format("%s%s", relation.getRequester().getUserNickname(), alarmTemplate.templateContent());

            Optional<AlarmBox> alarmBoxOptional = alarmBoxRepository
                    .findFirstByUserAndAlarmBoxContentAndIsCheckedOrderByAlarmBoxIdDesc(receiver, alarmContent, Status.N);
            alarmBoxOptional.ifPresent(AlarmBox::checkedAlarm);

        }

    }

    @Transactional
    public void deleteRelation(Long relationId) {
        Relation relation = relationRepository.findById(relationId)
                .orElseThrow(() -> new IllegalArgumentException("잘못 된 사용자입니다"));

        relationRepository.delete(relation);
    }
}