package com.linkee.linkeeapi.alarm_box.query.service;

import com.linkee.linkeeapi.alarm_box.query.dto.request.AlarmBoxSearchRequest;
import com.linkee.linkeeapi.alarm_box.query.dto.response.AlarmBoxResponse;
import com.linkee.linkeeapi.common.model.PageResponse;
import org.springframework.http.ResponseEntity;

public interface AlarmBoxQueryService {

    PageResponse<AlarmBoxResponse> selectAllAlarmBox(AlarmBoxSearchRequest request);
    ResponseEntity<AlarmBoxResponse> selectAlarmTemplateByAlarmBoxId(Long alarmBoxId);

}
