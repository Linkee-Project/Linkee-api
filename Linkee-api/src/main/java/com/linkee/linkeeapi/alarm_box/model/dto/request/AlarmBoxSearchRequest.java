package com.linkee.linkeeapi.alarm_box.model.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlarmBoxSearchRequest {

    private String keyword;
    private Integer page;
    private Integer size;
    private Integer offset;
    private String isChecked;

}
