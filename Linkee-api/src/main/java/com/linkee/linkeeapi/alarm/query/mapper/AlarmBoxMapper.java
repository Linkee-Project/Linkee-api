package com.linkee.linkeeapi.alarm.query.mapper;

import com.linkee.linkeeapi.alarm.query.dto.request.AlarmBoxSearchRequest;
import com.linkee.linkeeapi.alarm.query.dto.response.AlarmBoxResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AlarmBoxMapper {

    List<AlarmBoxResponse> selectAllAlarmBox(AlarmBoxSearchRequest requestMapper);

    int countAlarmBox(AlarmBoxSearchRequest requestMapper);

    AlarmBoxResponse selectAlarmBoxByBoxId(Long alarmBoxId);
}
