package com.linkee.linkeeapi.chat.chat_command.chat_domain.dto;

public record ChatMemberDto(
      Long chatMemberId, Long userId, String userNickname
) {}
