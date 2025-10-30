package com.linkee.linkeeapi.alarm_template.mapper;

import com.linkee.linkeeapi.alarm_template.model.dto.request.AlarmTemplateSearchRequest;
import com.linkee.linkeeapi.alarm_template.model.dto.response.AlarmTemplateResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AlarmTemplateMapper {

    List<AlarmTemplateResponse> selectAllAlarmTemplate(AlarmTemplateSearchRequest request);

    int countAlarmTemplate(AlarmTemplateSearchRequest request);

    AlarmTemplateResponse selectAlarmTemplateById(Long templateId);

}
