package com.linkee.linkeeapi.alarm.query.service;

import com.linkee.linkeeapi.alarm.query.dto.request.AlarmBoxSearchRequest;
import com.linkee.linkeeapi.alarm.query.dto.response.AlarmBoxResponse;
import com.linkee.linkeeapi.common.model.PageResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AlarmBoxQueryService {

    PageResponse<AlarmBoxResponse> selectAllAlarmBox(AlarmBoxSearchRequest request);
    ResponseEntity<AlarmBoxResponse> selectAlarmTemplateByAlarmBoxId(Long alarmBoxId);
    ResponseEntity<List<AlarmBoxResponse>> selectAlarmBoxByUserId(Long userId);
}
