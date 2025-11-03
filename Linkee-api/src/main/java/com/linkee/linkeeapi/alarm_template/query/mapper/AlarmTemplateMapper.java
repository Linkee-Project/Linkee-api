package com.linkee.linkeeapi.alarm_template.query.mapper;

import com.linkee.linkeeapi.alarm_template.query.dto.reqeust.AlarmTemplateSearchRequest;
import com.linkee.linkeeapi.alarm_template.query.dto.response.AlarmTemplateResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AlarmTemplateMapper {

    List<AlarmTemplateResponse> selectAllAlarmTemplate(AlarmTemplateSearchRequest request);

    int countAlarmTemplate(AlarmTemplateSearchRequest request);

    AlarmTemplateResponse selectAlarmTemplateById(Long templateId);

}
