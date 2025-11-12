package com.linkee.linkeeapi.alarm.command.application.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlarmBoxCreateRequest {

    private String alarmBoxContent;
    private Long userId;

}
