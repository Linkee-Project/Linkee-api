package com.linkee.linkeeapi.relation.query.service;

import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.relation.query.dto.request.RelationSearchRequest;
import com.linkee.linkeeapi.relation.query.dto.response.RelationResponse;
import com.linkee.linkeeapi.relation.query.mapper.RelationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RelationQueryService {

    private final RelationMapper relationMapper;


    public PageResponse<RelationResponse> selectAllRelations(RelationSearchRequest request) {

        int page = request.getPage() != null && request.getPage() > 0 ? request.getPage() : 0;
        int size = request.getSize() != null && request.getSize() > 0 ? request.getSize() : 10;
        int offset = page * size;

        // Mapper에 전달할 request (offset 포함)
        RelationSearchRequest requestMapper = RelationSearchRequest.builder()
                .userId(request.getUserId())
                .relationStatus(request.getRelationStatus())
                .page(page)
                .size(size)
                .offset(offset)
                .build();

        List<RelationResponse> results = relationMapper.selectAllRelations(requestMapper);
        int total = relationMapper.countAllRelations(requestMapper);

        return PageResponse.from(results, page, size, total);
    }



    // 내아이디 기준 조회
    public PageResponse<RelationResponse> selectRelationsByUser(RelationSearchRequest request) {

        int page = request.getPage() != null && request.getPage() > 0 ? request.getPage() : 0;
        int size = request.getSize() != null && request.getSize() > 0 ? request.getSize() : 10;
        int offset = page * size;

        // Mapper에 전달할 request (offset 포함)
        RelationSearchRequest requestMapper = RelationSearchRequest.builder()
                .userId(request.getUserId())
                .relationStatus(request.getRelationStatus())
                .page(page)
                .size(size)
                .offset(offset)
                .build();

        List<RelationResponse> results = relationMapper.selectRelationsByUserIdAndStatus(requestMapper);
        int total = relationMapper.countRelationsByUserIdAndStatus(requestMapper);

        return PageResponse.from(results, page, size, total);
    }
}
