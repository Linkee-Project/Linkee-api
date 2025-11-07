package com.linkee.linkeeapi.relation.command.infrastructure.repository;

import com.linkee.linkeeapi.relation.command.domain.aggregate.entity.Relation;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RelationRepository extends JpaRepository<Relation,Long> {

    /**
     * 친구 요청 기능
     * */
    @Query("SELECT r " +
            "FROM Relation r " +
            "WHERE (r.requester = :user1 " +
            "AND r.receiver = :user2)" +
            "OR (r.requester = :user2 " +
            "AND r.receiver = :user1)")
    // findByUser -> 두 사용자 간의 관계를 찾아주기 위해서 생성
    Optional<Relation> findByUsers(@Param("user1") User user1, @Param("user2") User user2);

    void deleteByReceiverOrRequester(User receiver, User requester);

}
