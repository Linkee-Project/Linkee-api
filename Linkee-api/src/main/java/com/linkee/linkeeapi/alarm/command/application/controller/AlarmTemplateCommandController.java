package com.linkee.linkeeapi.alarm.command.application.controller;

import com.linkee.linkeeapi.alarm.command.application.dto.request.AlarmTemplateCreateRequest;
import com.linkee.linkeeapi.alarm.command.application.service.AlarmTemplateCommandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "알림", description = "알림 템플릿 및 발송 관리 API")
public class AlarmTemplateCommandController {

    private final AlarmTemplateCommandService service;

    // create
    @PostMapping("/admin/alarm/templates")
    public ResponseEntity<String> createAlarmTemplate(@RequestBody AlarmTemplateCreateRequest request){
        service.createAlarmTemplate(request);

        return ResponseEntity.ok("저장 완료");
    }


    // update
    @PatchMapping("/admin/alarm/templates/modify/{templateId}")
    public ResponseEntity<String> modifyAlarmTemplate(@PathVariable long templateId , @RequestBody AlarmTemplateCreateRequest request ){

        service.modifyAlarmTemplateByAlarmTemplateId(templateId,request);

        return ResponseEntity.ok("저장 완료");
    }

    // delete
    @DeleteMapping("/admin/alarm/templates/{templateId}")
    public ResponseEntity<String> deleteAlarmTemplate(@PathVariable long templateId) {
        service.deleteAlarmTemplate(templateId);
        return ResponseEntity.ok("삭제 완료");
    }

}
