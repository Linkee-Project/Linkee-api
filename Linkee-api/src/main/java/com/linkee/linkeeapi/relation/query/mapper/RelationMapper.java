package com.linkee.linkeeapi.relation.query.mapper;

import com.linkee.linkeeapi.relation.query.dto.request.RelationSearchRequest;
import com.linkee.linkeeapi.relation.query.dto.response.RelationResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RelationMapper {

    List<RelationResponse> selectAllRelations(RelationSearchRequest request);
    int countAllRelations(RelationSearchRequest request);

    // id 기준
    List<RelationResponse> selectRelationsByUserIdAndStatus(RelationSearchRequest request);
    int countRelationsByUserIdAndStatus(RelationSearchRequest request);
}
