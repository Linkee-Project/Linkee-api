package com.linkee.linkeeapi.alarm.command.application.service;

import com.linkee.linkeeapi.alarm.command.application.dto.request.AlarmTemplateCreateRequest;
import com.linkee.linkeeapi.alarm.command.domain.aggregate.entity.AlarmBox;
import com.linkee.linkeeapi.alarm.command.instructure.repository.AlarmTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmTemplateCommandServiceImpl implements AlarmTemplateCommandService {

    private final AlarmTemplateRepository repository;

    @Override
    public void createAlarmTemplate(AlarmTemplateCreateRequest request) {

        AlarmBox.AlarmTemplate alarmTemplate = AlarmBox.AlarmTemplate.builder()
                .templateContent(request.templateContent())
                .build();

        repository.save(alarmTemplate);
    }


    @Override
    public void modifyAlarmTemplateByAlarmTemplateId(Long templateId, AlarmTemplateCreateRequest request) {
        AlarmBox.AlarmTemplate foundTemplate = repository.findById(templateId).orElseThrow();

        if(!(request.templateContent().isBlank() || request.templateContent() == null)){
            foundTemplate.modifyTemplateContent(request.templateContent());
        }
    }
}
