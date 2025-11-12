package com.linkee.linkeeapi.alarm.query.dto.request;

public record AlarmTemplateSearchRequest(
        String keyword,
        Integer page,
        Integer size,
        Integer offset
) {}
