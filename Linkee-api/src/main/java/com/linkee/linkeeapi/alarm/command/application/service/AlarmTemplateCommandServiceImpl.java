package com.linkee.linkeeapi.alarm.command.application.service;

import com.linkee.linkeeapi.alarm.command.application.dto.request.AlarmTemplateCreateRequest;
import com.linkee.linkeeapi.alarm.command.domain.aggregate.entity.AlarmTemplate;
import com.linkee.linkeeapi.alarm.command.instructure.repository.AlarmTemplateRepository;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmTemplateCommandServiceImpl implements AlarmTemplateCommandService {

    private final AlarmTemplateRepository repository;

    @Override
    @Transactional // Added @Transactional
    public void createAlarmTemplate(AlarmTemplateCreateRequest request) {
        // Check if templateCode is provided
        if (request.templateCode() == null || request.templateCode().isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Template code cannot be null or blank.");
        }
        // Check for duplicate template code
        if (repository.findByTemplateCode(request.templateCode()).isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATE_ALARM_TEMPLATE_CODE);
        }

        AlarmTemplate alarmTemplate =AlarmTemplate.builder()
                .templateContent(request.templateContent())
                .templateCode(request.templateCode())
                .build();

        repository.save(alarmTemplate);
    }


    @Override
    @Transactional // Added @Transactional
    public void modifyAlarmTemplateByAlarmTemplateId(Long templateId, AlarmTemplateCreateRequest request) {
        AlarmTemplate foundTemplate = repository.findById(templateId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ALARM_TEMPLATE_NOT_FOUND)); // Use specific error code

        // Update templateContent if provided
        if(request.templateContent() != null && !request.templateContent().isBlank()){
            foundTemplate.modifyTemplateContent(request.templateContent());
        }

        // Update templateCode if provided and different
        if(request.templateCode() != null && !request.templateCode().isBlank()){
            if (!foundTemplate.getTemplateCode().equals(request.templateCode())) { // Only check if code has changed
                // Check for duplicate template code
                repository.findByTemplateCode(request.templateCode()).ifPresent(existingTemplate -> {
                    if (!existingTemplate.getTemplateId().equals(templateId)) {
                        throw new BusinessException(ErrorCode.DUPLICATE_ALARM_TEMPLATE_CODE);
                    }
                });
                foundTemplate.modifyTemplateCode(request.templateCode());
            }
        }
    }

    @Override
    @Transactional
    public void deleteAlarmTemplate(long templateId) {
        AlarmTemplate foundTemplate = repository.findById(templateId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ALARM_TEMPLATE_NOT_FOUND));

        repository.delete(foundTemplate);
    }
}
