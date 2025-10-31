package com.linkee.linkeeapi.alarm_template.command.infrastructure.repository;

import com.linkee.linkeeapi.alarm_template.command.domain.aggregate.entity.AlarmTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmTemplateRepository extends JpaRepository<AlarmTemplate,Long> {
}
