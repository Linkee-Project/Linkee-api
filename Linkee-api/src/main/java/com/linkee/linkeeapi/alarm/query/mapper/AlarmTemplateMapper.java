package com.linkee.linkeeapi.alarm.query.mapper;

import com.linkee.linkeeapi.alarm.query.dto.request.AlarmTemplateSearchRequest;
import com.linkee.linkeeapi.alarm.query.dto.response.AlarmTemplateResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AlarmTemplateMapper {

    List<AlarmTemplateResponse> selectAllAlarmTemplate(AlarmTemplateSearchRequest request);

    int countAlarmTemplate(AlarmTemplateSearchRequest request);

    AlarmTemplateResponse selectAlarmTemplateById(Long templateId);

}
