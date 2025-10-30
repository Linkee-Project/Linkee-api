package com.linkee.linkeeapi.alarm_template.controller;

import com.linkee.linkeeapi.alarm_template.model.dto.request.AlarmTemplateCreateRequest;
import com.linkee.linkeeapi.alarm_template.model.dto.request.AlarmTemplateSearchRequest;
import com.linkee.linkeeapi.alarm_template.model.dto.response.AlarmTemplateResponse;
import com.linkee.linkeeapi.alarm_template.service.AlarmTemplateService;
import com.linkee.linkeeapi.common.model.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ap1/v1/alarm_templates")
public class AlarmTemplateController {

    private final AlarmTemplateService service;

    // create
    @PostMapping
    public ResponseEntity<String> createAlarmTemplate(@RequestBody String content){
        AlarmTemplateCreateRequest request = new AlarmTemplateCreateRequest(content);
        service.createAlarmTemplate(request);

        return ResponseEntity.ok("저장 완료");
    }

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


    @Transactional
    @PatchMapping("/modify/{templateId}")
    public ResponseEntity<String> modifyAlarmTemplate(@PathVariable long templateId , @RequestBody AlarmTemplateCreateRequest request ){

        service.modifyAlarmTemplateByAlarmTemplateId(templateId,request);

        return ResponseEntity.ok("저장 완료");
    }


}
