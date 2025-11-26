package com.linkee.linkeeapi.alarm.command.instructure.repository;

import com.linkee.linkeeapi.alarm.command.domain.aggregate.entity.AlarmTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional; // Import Optional

public interface AlarmTemplateRepository extends JpaRepository<AlarmTemplate,Long> {
    Optional<AlarmTemplate> findByTemplateCode(String templateCode); // New method
}
