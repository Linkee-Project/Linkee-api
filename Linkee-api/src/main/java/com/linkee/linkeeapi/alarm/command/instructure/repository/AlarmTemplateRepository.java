package com.linkee.linkeeapi.alarm.command.instructure.repository;

import com.linkee.linkeeapi.alarm.command.domain.aggregate.entity.AlarmTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmTemplateRepository extends JpaRepository<AlarmTemplate,Long> {
}
