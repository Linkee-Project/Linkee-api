package com.linkee.linkeeapi.alarm_box.command.application.service;

import com.linkee.linkeeapi.alarm_box.command.application.dto.request.AlarmBoxCreateRequest;
import com.linkee.linkeeapi.alarm_box.command.domain.aggregate.entity.AlarmBox;
import com.linkee.linkeeapi.alarm_box.command.infrastructure.repository.AlarmBoxRepository;
import com.linkee.linkeeapi.common.enums.Role;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.user.model.entity.User;
import com.linkee.linkeeapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AlarmBoxCommandServiceImpl implements AlarmBoxCommandService {

    private final AlarmBoxRepository alarmBoxRepository;
    private final UserRepository userRepository;


    // alarmBox 생성
    @Override
    public void createAlarmBox(AlarmBoxCreateRequest request) {
        // 현재 데이터가 없으므로 user 는 임의로 집어넣어준다
        User foundUser = userRepository.findById(request.getUserId())
                .orElseGet(() -> userRepository.save(
                        new User(null,
                                "user00"+request.getUserId(),
                                "pass00"+request.getUserId(),
                                "짱이"+request.getUserId(),
                                LocalDateTime.now(), LocalDateTime.now(), Status.Y, Role.USER)
                ));

        AlarmBox alarmBox = AlarmBox.builder()
                .alarmBoxContent(request.getAlarmBoxContent())
                .user(foundUser)
                .isChecked(Status.N)
                .build();

        alarmBoxRepository.save(alarmBox);
    }

    // 알람박스 읽음 확인
    @Transactional
    @Override
    public void checkedAlarmBox(Long alarmBoxId) {

        AlarmBox alarmBox = alarmBoxRepository.findById(alarmBoxId).orElseThrow();

        alarmBox.checkedAlarm();
    }

    // 알람박스 삭제
    @Transactional
    @Override
    public void deleteAlarmBoxById(Long alarmBoxId){
        if(alarmBoxRepository.findById(alarmBoxId).isPresent()) {

            alarmBoxRepository.deleteById(alarmBoxId);

        }
    }
}
