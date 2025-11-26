package com.linkee.linkeeapi.alarm.command.application.dto.request;

public record AlarmTemplateCreateRequest(
        String templateContent,
        String templateCode // Added templateCode
) {
}
