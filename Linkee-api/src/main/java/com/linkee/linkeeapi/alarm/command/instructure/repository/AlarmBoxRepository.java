package com.linkee.linkeeapi.alarm.command.instructure.repository;

import com.linkee.linkeeapi.alarm.command.domain.aggregate.entity.AlarmBox;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.users.command.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlarmBoxRepository extends JpaRepository<AlarmBox,Long> {
    Optional<AlarmBox> findFirstByUserAndAlarmBoxContentAndIsCheckedOrderByAlarmBoxIdDesc(User user, String alarmBoxContent, Status isChecked);

    // 전체 알림 확인
    List<AlarmBox> findByUserAndIsChecked(User user, Status isChecked);

}
