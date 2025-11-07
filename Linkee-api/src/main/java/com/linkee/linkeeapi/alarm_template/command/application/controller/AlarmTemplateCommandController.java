package com.linkee.linkeeapi.alarm_template.command.application.controller;

import com.linkee.linkeeapi.alarm_template.command.application.dto.request.AlarmTemplateCreateRequest;
import com.linkee.linkeeapi.alarm_template.command.application.service.AlarmTemplateCommandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ap1/v1/alarm_templates")
@Tag(name = "알림", description = "알림 템플릿 및 발송 관리 API")
public class AlarmTemplateCommandController {

    private final AlarmTemplateCommandService service;

    // create
    @PostMapping
    public ResponseEntity<String> createAlarmTemplate(@RequestBody String content){
        AlarmTemplateCreateRequest request = new AlarmTemplateCreateRequest(content);
        service.createAlarmTemplate(request);

        return ResponseEntity.ok("저장 완료");
    }


    // update
    @Transactional
    @PatchMapping("/modify/{templateId}")
    public ResponseEntity<String> modifyAlarmTemplate(@PathVariable long templateId , @RequestBody AlarmTemplateCreateRequest request ){

        service.modifyAlarmTemplateByAlarmTemplateId(templateId,request);

        return ResponseEntity.ok("저장 완료");
    }

}
