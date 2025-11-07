package com.linkee.linkeeapi.relation.command.application.controller;

import com.linkee.linkeeapi.relation.command.application.dto.request.RelationCreateRequest;
import com.linkee.linkeeapi.relation.command.application.service.RelationCommandService;
import com.linkee.linkeeapi.relation.command.domain.aggregate.entity.RelationStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/relations")
@Tag(name = "커뮤니케이션", description = "친구 및 채팅 기능 관련 API")
public class RelationCommandController {

    private final RelationCommandService relationCommandService;


    @PostMapping
    public String createRelation(@RequestBody RelationCreateRequest request) {
        relationCommandService.createRelation(request);
        return "친구 요청 생성 완료";
    }


    @PatchMapping("/{relationId}")
    public String updateRelationStatus(@PathVariable Long relationId,
                                       @RequestParam RelationStatus status) {
        relationCommandService.updateRelationStatus(relationId, status);
        return "친구 상태 업데이트 완료";
    }


    @DeleteMapping("/{relationId}")
    public String deleteRelation(@PathVariable Long relationId) {
        relationCommandService.deleteRelation(relationId);
        return "친구 관계 삭제 완료";
    }
}
