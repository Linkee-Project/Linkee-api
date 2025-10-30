package com.linkee.linkeeapi.alarm_box.mapper;

import com.linkee.linkeeapi.alarm_box.model.dto.request.AlarmBoxSearchRequest;
import com.linkee.linkeeapi.alarm_box.model.dto.response.AlarmBoxResponse;
import com.linkee.linkeeapi.alarm_template.model.dto.response.AlarmTemplateResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AlarmBoxMapper {

    List<AlarmBoxResponse> selectAllAlarmBox(AlarmBoxSearchRequest requestMapper);

    int countAlarmBox(AlarmBoxSearchRequest requestMapper);

    AlarmBoxResponse selectAlarmBoxByBoxId(Long alarmBoxId);
}
