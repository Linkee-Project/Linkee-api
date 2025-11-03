package com.linkee.linkeeapi.alarm_box.command.infrastructure.repository;

import com.linkee.linkeeapi.alarm_box.command.domain.aggregate.entity.AlarmBox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmBoxRepository extends JpaRepository<AlarmBox,Long> {
}
