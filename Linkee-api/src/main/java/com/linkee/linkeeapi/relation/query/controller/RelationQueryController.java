package com.linkee.linkeeapi.relation.query.controller;


import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.relation.query.dto.request.RelationSearchRequest;
import com.linkee.linkeeapi.relation.query.dto.response.RelationResponse;
import com.linkee.linkeeapi.relation.query.service.RelationQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/relations")
public class RelationQueryController {

    private final RelationQueryService relationQueryService;

    @GetMapping("/all")
    public PageResponse<RelationResponse> getAllRelations(RelationSearchRequest request) {

        return relationQueryService.selectAllRelations(request);
    }

    // 내 id 기준
    @GetMapping("/my/{userId}")
    public PageResponse<RelationResponse> getMyRelations(
            @PathVariable Long userId,
            RelationSearchRequest request
    ) {

        request.setUserId(userId);
        return relationQueryService.selectRelationsByUser(request);
    }

}
