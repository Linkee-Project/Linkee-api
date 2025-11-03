package com.linkee.linkeeapi.relation.command.application.service;

import com.linkee.linkeeapi.relation.command.application.dto.request.RelationCreateRequest;
import com.linkee.linkeeapi.relation.command.domain.aggregate.entity.Relation;
import com.linkee.linkeeapi.relation.command.domain.aggregate.entity.RelationStatus;
import com.linkee.linkeeapi.relation.command.infrastructure.repository.RelationRepository;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user.command.infrastructure.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RelationCommandService {

    private final RelationRepository relationRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createRelation(RelationCreateRequest request) {

        User requester = userRepository.findById(request.getRequesterId())
                .orElseThrow(() -> new IllegalArgumentException("잘못 된 사용자입니다"));
        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new IllegalArgumentException("잘못 된 사용자입니다"));


        Relation relation = Relation.builder()
                .requester(requester)
                .receiver(receiver)
                .build();

        relationRepository.save(relation);
    }


    @Transactional
    public void updateRelationStatus(Long relationId, RelationStatus status) {
        Relation relation = relationRepository.findById(relationId)
                .orElseThrow(() -> new IllegalArgumentException("잘못 된 사용자입니다"));

        relation.modifyRelationStatus(status);

    }

    @Transactional
    public void deleteRelation(Long relationId) {
        Relation relation = relationRepository.findById(relationId)
                .orElseThrow(() -> new IllegalArgumentException("잘못 된 사용자입니다"));

        relationRepository.delete(relation);
    }
}