package com.linkee.linkeeapi.alarm_template.query.service;

import com.linkee.linkeeapi.alarm_template.query.dto.reqeust.AlarmTemplateSearchRequest;
import com.linkee.linkeeapi.alarm_template.query.dto.response.AlarmTemplateResponse;
import com.linkee.linkeeapi.common.model.PageResponse;
import org.springframework.http.ResponseEntity;

public interface AlarmTemplateQueryService {



    PageResponse<AlarmTemplateResponse> selectAllAlarmTemplate(AlarmTemplateSearchRequest request);
    ResponseEntity<AlarmTemplateResponse> selectAlarmTemplateByAlarmTemplateId(Long templateId);



}
