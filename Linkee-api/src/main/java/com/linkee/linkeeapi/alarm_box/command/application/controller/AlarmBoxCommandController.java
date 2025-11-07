package com.linkee.linkeeapi.alarm_box.command.application.controller;

import com.linkee.linkeeapi.alarm_box.command.application.dto.request.AlarmBoxCreateRequest;
import com.linkee.linkeeapi.alarm_box.command.application.service.AlarmBoxCommandService;
import com.linkee.linkeeapi.common.security.model.CustomUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/alarm_boxes")
@Tag(name = "알림", description = "알림 템플릿 및 발송 관리 API")
public class AlarmBoxCommandController {

    private final AlarmBoxCommandService service;


    @PostMapping
    public ResponseEntity<String> createAlarmBoxContent(@RequestBody AlarmBoxCreateRequest request){
        service.createAlarmBox(request);
        return ResponseEntity.ok("알람박스 생성 성공");
    }


    @PatchMapping("/check/{alarmBoxId}")
    public ResponseEntity<String> checkedAlarm(@PathVariable Long alarmBoxId){
        service.checkedAlarmBox(alarmBoxId);

        return ResponseEntity.ok("알림확인!");
    }
    @DeleteMapping("/delete/{alarmBoxId}")
    public ResponseEntity<String> deleteAlarm(@PathVariable Long alarmBoxId){
        service.deleteAlarmBoxById(alarmBoxId);
        return ResponseEntity.ok("삭제 성공");
    }

    @PatchMapping("/check-all")
    public ResponseEntity<String> checkAllAlarms(@AuthenticationPrincipal CustomUser customUser) {
        Long userId = customUser.getUserId();

        service.checkAllAlarms(userId); // userId를 서비스에 전달

        return ResponseEntity.ok("모든 알림을 확인했습니다.");
    }
}
