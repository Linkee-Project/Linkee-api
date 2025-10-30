package com.linkee.linkeeapi.alarm_template.service;

import com.linkee.linkeeapi.alarm_template.mapper.AlarmTemplateMapper;
import com.linkee.linkeeapi.alarm_template.model.dto.request.AlarmTemplateCreateRequest;
import com.linkee.linkeeapi.alarm_template.model.dto.request.AlarmTemplateSearchRequest;
import com.linkee.linkeeapi.alarm_template.model.dto.response.AlarmTemplateResponse;
import com.linkee.linkeeapi.alarm_template.model.entity.AlarmTemplate;
import com.linkee.linkeeapi.alarm_template.repository.AlarmTemplateRepository;
import com.linkee.linkeeapi.common.model.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmTemplateServiceImpl implements AlarmTemplateService{

    private final AlarmTemplateRepository repository;
    private final AlarmTemplateMapper alarmTemplateMapper;

    @Override
    public void createAlarmTemplate(AlarmTemplateCreateRequest request) {

       AlarmTemplate alarmTemplate = AlarmTemplate.builder()
               .templateContent(request.templateContent())
               .build();

        repository.save(alarmTemplate);
    }

    @Override
    public PageResponse<AlarmTemplateResponse> selectAllAlarmTemplate(AlarmTemplateSearchRequest request) {

        int page = request.page() != null && request.page() >= 0 ? request.page() : 0;
        int size = request.size() != null && request.size() > 0 ? request.size() : 10;
        int offset = page * size;

        // Mapper에 전달할 record 생성 (offset 포함)
        AlarmTemplateSearchRequest requestMapper = new AlarmTemplateSearchRequest(
                request.keyword(),
                page,
                size,
                offset
        );
        List<AlarmTemplateResponse> templates = alarmTemplateMapper.selectAllAlarmTemplate(requestMapper);


        int total = alarmTemplateMapper.countAlarmTemplate(requestMapper);

        return PageResponse.from(templates, page, size, total);
    }


    // id 값으로 단건 조회
    @Override
    public ResponseEntity<AlarmTemplateResponse> selectAlarmTemplateByAlarmTemplateId(Long templateId) {

        AlarmTemplateResponse template = alarmTemplateMapper.selectAlarmTemplateById(templateId);

        return ResponseEntity.ok(template);
    }


    @Override
    public void modifyAlarmTemplateByAlarmTemplateId(Long templateId, AlarmTemplateCreateRequest request) {
        AlarmTemplate foundTemplate = repository.findById(templateId).orElseThrow();

        if(!(request.templateContent().isBlank() || request.templateContent() == null)){
        foundTemplate.modifyTemplateContent(request.templateContent());
        }
    }



}
