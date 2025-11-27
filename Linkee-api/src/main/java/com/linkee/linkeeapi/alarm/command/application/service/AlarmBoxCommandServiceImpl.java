package com.linkee.linkeeapi.alarm.command.application.service;

import com.linkee.linkeeapi.alarm.command.application.dto.request.AlarmBoxCreateRequest;
import com.linkee.linkeeapi.alarm.command.domain.aggregate.entity.AlarmBox;
import com.linkee.linkeeapi.alarm.command.instructure.repository.AlarmBoxRepository;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.users.command.domain.entity.User;
import com.linkee.linkeeapi.users.command.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlarmBoxCommandServiceImpl implements AlarmBoxCommandService {

    private final AlarmBoxRepository alarmBoxRepository;
    private final UserRepository userRepository;


    // alarmBox 생성
    @Override
    @Transactional
    public void createAlarmBox(AlarmBoxCreateRequest request) {
        Optional<User> foundUserOptional = userRepository.findById(request.getUserId());
        if (foundUserOptional.isEmpty()) {
            log.error("알림을 생성하려는 사용자를 찾을 수 없습니다. userId: {}", request.getUserId());
            return;
        }
        User foundUser = foundUserOptional.get();

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

    // 전체 알림 확인
    @Transactional
    @Override
    public void checkAllAlarms(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<AlarmBox> unreadAlarms = alarmBoxRepository.findByUserAndIsChecked(user, Status.N);

        for (AlarmBox alarm : unreadAlarms) {
            alarm.checkedAlarm();
        }
    }
}
