package com.linkee.linkeeapi.alarm_template.command.application.service;

import com.linkee.linkeeapi.alarm_template.command.application.dto.request.AlarmTemplateCreateRequest;
import com.linkee.linkeeapi.alarm_template.command.domain.aggregate.entity.AlarmTemplate;
import com.linkee.linkeeapi.alarm_template.command.infrastructure.repository.AlarmTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmTemplateCommandServiceImpl implements AlarmTemplateCommandService {

    private final AlarmTemplateRepository repository;

    @Override
    public void createAlarmTemplate(AlarmTemplateCreateRequest request) {

        AlarmTemplate alarmTemplate = AlarmTemplate.builder()
                .templateContent(request.templateContent())
                .build();

        repository.save(alarmTemplate);
    }


    @Override
    public void modifyAlarmTemplateByAlarmTemplateId(Long templateId, AlarmTemplateCreateRequest request) {
        AlarmTemplate foundTemplate = repository.findById(templateId).orElseThrow();

        if(!(request.templateContent().isBlank() || request.templateContent() == null)){
            foundTemplate.modifyTemplateContent(request.templateContent());
        }
    }
}
