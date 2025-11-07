package com.linkee.linkeeapi.qna.command.application.service;

import com.linkee.linkeeapi.qna.command.application.dto.request.CreateQnaRequestDto;

public interface QnaCommandService {

    void createQna(CreateQnaRequestDto request, Long userId);
}
