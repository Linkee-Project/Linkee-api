package com.linkee.linkeeapi.relation.command.infrastructure.repository;

import com.linkee.linkeeapi.relation.command.domain.aggregate.entity.Relation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelationRepository extends JpaRepository<Relation,Long> {
}
