package com.linkee.linkeeapi.alarm.query.dto.response;

import com.fasterxml.jackson.annotation.JsonRawValue;

import java.time.LocalDateTime;

public record AlarmTemplateResponse(
        Long templateId,
        @JsonRawValue
        String templateContent,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
