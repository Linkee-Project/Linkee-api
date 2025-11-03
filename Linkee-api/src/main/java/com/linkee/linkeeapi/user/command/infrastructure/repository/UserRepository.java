package com.linkee.linkeeapi.user.command.infrastructure.repository;

import com.linkee.linkeeapi.user.command.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserEmail(String userEmail);
}
