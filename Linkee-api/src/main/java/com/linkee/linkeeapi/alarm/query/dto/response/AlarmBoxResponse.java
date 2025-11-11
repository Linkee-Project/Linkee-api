package com.linkee.linkeeapi.alarm.query.dto.response;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlarmBoxResponse {
    private Long alarmBoxId;
    private String alarmBoxContent;
    private String isChecked;
    private LocalDateTime createdAt;
    private Long userId;
}
