package com.linkee.linkeeapi.alarm_box.service;

import com.linkee.linkeeapi.alarm_box.model.dto.request.AlarmBoxSearchRequest;
import com.linkee.linkeeapi.alarm_box.model.dto.response.AlarmBoxResponse;
import com.linkee.linkeeapi.alarm_box.model.entity.AlarmBox;
import com.linkee.linkeeapi.alarm_template.model.dto.request.AlarmTemplateSearchRequest;
import com.linkee.linkeeapi.alarm_template.model.dto.response.AlarmTemplateResponse;
import com.linkee.linkeeapi.common.model.PageResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AlarmBoxService {

    PageResponse<AlarmBoxResponse> selectAllAlarmBox(AlarmBoxSearchRequest request);
    ResponseEntity<AlarmBoxResponse> selectAlarmTemplateByAlarmBoxId(Long alarmBoxId);
}
