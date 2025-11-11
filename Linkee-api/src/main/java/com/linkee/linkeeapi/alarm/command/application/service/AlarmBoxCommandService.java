package com.linkee.linkeeapi.alarm.command.application.service;

import com.linkee.linkeeapi.alarm.command.application.dto.request.AlarmBoxCreateRequest;

public interface AlarmBoxCommandService {


    void createAlarmBox(AlarmBoxCreateRequest request);
    void checkedAlarmBox(Long alarmBoxId);
    void deleteAlarmBoxById(Long alarmBoxId);

    void checkAllAlarms(Long userId);

}
