package com.linkee.linkeeapi.chat.command.application.service.services;

import com.linkee.linkeeapi.chat.command.application.dto.request.CreateQnaRequestDto;

public interface QnaCommandService {

    void createQna(CreateQnaRequestDto request, Long userId);
}
