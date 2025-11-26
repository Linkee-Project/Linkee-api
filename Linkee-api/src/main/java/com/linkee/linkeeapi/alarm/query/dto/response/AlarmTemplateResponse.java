package com.linkee.linkeeapi.alarm.query.dto.response;

import com.fasterxml.jackson.annotation.JsonRawValue;

import java.time.LocalDateTime;

public record AlarmTemplateResponse(
        Long templateId,
        String templateContent, // Removed @JsonRawValue
        String templateCode,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
