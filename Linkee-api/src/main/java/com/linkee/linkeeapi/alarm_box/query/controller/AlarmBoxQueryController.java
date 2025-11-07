package com.linkee.linkeeapi.alarm_box.query.controller;

import com.linkee.linkeeapi.alarm_box.query.dto.request.AlarmBoxSearchRequest;
import com.linkee.linkeeapi.alarm_box.query.dto.response.AlarmBoxResponse;
import com.linkee.linkeeapi.alarm_box.query.service.AlarmBoxQueryService;
import com.linkee.linkeeapi.common.model.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ap1/v1/alarm_boxes")
@Tag(name = "알림", description = "알림 템플릿 및 발송 관리 API")
public class AlarmBoxQueryController {

    private final AlarmBoxQueryService service;

    @GetMapping
    public PageResponse<AlarmBoxResponse> getAllAlarmTemplates(
            AlarmBoxSearchRequest request) {

        return service.selectAllAlarmBox(request);
    }

    @GetMapping("/{alarmBoxId}")
    public ResponseEntity<AlarmBoxResponse> getAlarmBoxById(@PathVariable long alarmBoxId){

        return service.selectAlarmTemplateByAlarmBoxId(alarmBoxId);
    }


}
