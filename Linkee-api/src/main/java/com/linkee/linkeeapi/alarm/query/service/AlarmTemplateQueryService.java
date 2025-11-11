package com.linkee.linkeeapi.alarm.query.service;

import com.linkee.linkeeapi.alarm.query.dto.request.AlarmTemplateSearchRequest;
import com.linkee.linkeeapi.alarm.query.dto.response.AlarmTemplateResponse;
import com.linkee.linkeeapi.common.model.PageResponse;
import org.springframework.http.ResponseEntity;

public interface AlarmTemplateQueryService {



    PageResponse<AlarmTemplateResponse> selectAllAlarmTemplate(AlarmTemplateSearchRequest request);
    ResponseEntity<AlarmTemplateResponse> selectAlarmTemplateByAlarmTemplateId(Long templateId);



}
