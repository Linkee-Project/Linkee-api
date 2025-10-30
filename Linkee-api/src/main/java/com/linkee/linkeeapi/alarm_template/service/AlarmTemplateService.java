package com.linkee.linkeeapi.alarm_template.service;

import com.linkee.linkeeapi.alarm_template.model.dto.request.AlarmTemplateCreateRequest;
import com.linkee.linkeeapi.alarm_template.model.dto.request.AlarmTemplateSearchRequest;
import com.linkee.linkeeapi.alarm_template.model.dto.response.AlarmTemplateResponse;
import com.linkee.linkeeapi.alarm_template.model.entity.AlarmTemplate;
import com.linkee.linkeeapi.common.model.PageResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AlarmTemplateService {

    void createAlarmTemplate(AlarmTemplateCreateRequest request);

    PageResponse<AlarmTemplateResponse> selectAllAlarmTemplate(AlarmTemplateSearchRequest request);
    ResponseEntity<AlarmTemplateResponse> selectAlarmTemplateByAlarmTemplateId(Long templateId);


    void modifyAlarmTemplateByAlarmTemplateId(Long templateId,AlarmTemplateCreateRequest request);

}
