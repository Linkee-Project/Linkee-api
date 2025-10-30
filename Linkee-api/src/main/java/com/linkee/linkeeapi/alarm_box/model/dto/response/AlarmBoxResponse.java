package com.linkee.linkeeapi.alarm_box.model.dto.response;

import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.user.model.entity.User;
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
    private String status;
    private LocalDateTime createAt;
    private Long userId;
}
