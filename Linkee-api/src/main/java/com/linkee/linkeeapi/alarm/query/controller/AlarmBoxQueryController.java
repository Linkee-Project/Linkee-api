package com.linkee.linkeeapi.alarm.query.controller;

import com.linkee.linkeeapi.alarm.query.dto.request.AlarmBoxSearchRequest;
import com.linkee.linkeeapi.alarm.query.dto.response.AlarmBoxResponse;
import com.linkee.linkeeapi.alarm.query.service.AlarmBoxQueryService;
import com.linkee.linkeeapi.common.model.CustomUser;
import com.linkee.linkeeapi.common.model.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarm/boxes")
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

    @GetMapping("/me")
    public ResponseEntity<List<AlarmBoxResponse>> getAlarmBoxUserId(@AuthenticationPrincipal CustomUser customUser){


        Long userId = customUser.getUserId();

        return service.selectAlarmBoxByUserId(userId);
    }


}
