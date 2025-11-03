package com.linkee.linkeeapi.alarm_template.query.dto.reqeust;

public record AlarmTemplateSearchRequest(
        String keyword,
        Integer page,
        Integer size,
        Integer offset
) {}
