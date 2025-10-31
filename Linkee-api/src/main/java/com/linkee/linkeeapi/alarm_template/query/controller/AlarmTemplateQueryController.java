package com.linkee.linkeeapi.alarm_template.query.controller;

import com.linkee.linkeeapi.alarm_template.query.dto.reqeust.AlarmTemplateSearchRequest;
import com.linkee.linkeeapi.alarm_template.query.dto.response.AlarmTemplateResponse;
import com.linkee.linkeeapi.alarm_template.query.service.AlarmTemplateQueryService;
import com.linkee.linkeeapi.common.model.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ap1/v1/alarm_templates")
public class AlarmTemplateQueryController {

    private final AlarmTemplateQueryService service;



    // selectAll
    @GetMapping
    public PageResponse<AlarmTemplateResponse> getAllAlarmTemplates(
            AlarmTemplateSearchRequest request) {

        return service.selectAllAlarmTemplate(request);
    }

    // selectById
    @GetMapping("/{templateId}")
    public ResponseEntity<AlarmTemplateResponse> getAlarmTemplateById(@PathVariable Long templateId){

        return service.selectAlarmTemplateByAlarmTemplateId(templateId);
    }



}
