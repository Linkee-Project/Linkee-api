package com.linkee.linkeeapi.alarm.command.application.service;


import com.linkee.linkeeapi.alarm.command.application.dto.request.AlarmTemplateCreateRequest;

public interface AlarmTemplateCommandService {

    void createAlarmTemplate(AlarmTemplateCreateRequest request);
    void modifyAlarmTemplateByAlarmTemplateId(Long templateId,AlarmTemplateCreateRequest request);

}
