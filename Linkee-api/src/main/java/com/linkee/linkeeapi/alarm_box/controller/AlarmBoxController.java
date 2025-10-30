package com.linkee.linkeeapi.alarm_box.controller;

import com.linkee.linkeeapi.alarm_box.model.dto.request.AlarmBoxSearchRequest;
import com.linkee.linkeeapi.alarm_box.model.dto.response.AlarmBoxResponse;
import com.linkee.linkeeapi.alarm_box.service.AlarmBoxService;
import com.linkee.linkeeapi.alarm_template.model.dto.request.AlarmTemplateSearchRequest;
import com.linkee.linkeeapi.alarm_template.model.dto.response.AlarmTemplateResponse;
import com.linkee.linkeeapi.common.model.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ap1/v1/alarm_boxes")
public class AlarmBoxController {

    private final AlarmBoxService service;

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
