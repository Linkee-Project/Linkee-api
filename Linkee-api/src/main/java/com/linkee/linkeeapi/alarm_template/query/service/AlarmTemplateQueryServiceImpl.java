package com.linkee.linkeeapi.alarm_template.query.service;

import com.linkee.linkeeapi.alarm_template.query.mapper.AlarmTemplateMapper;
import com.linkee.linkeeapi.alarm_template.query.dto.reqeust.AlarmTemplateSearchRequest;
import com.linkee.linkeeapi.alarm_template.query.dto.response.AlarmTemplateResponse;
import com.linkee.linkeeapi.alarm_template.command.infrastructure.repository.AlarmTemplateRepository;
import com.linkee.linkeeapi.common.model.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmTemplateQueryServiceImpl implements AlarmTemplateQueryService {

    private final AlarmTemplateRepository repository;
    private final AlarmTemplateMapper alarmTemplateMapper;


    // 리스트 조회 및 검색
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




}
