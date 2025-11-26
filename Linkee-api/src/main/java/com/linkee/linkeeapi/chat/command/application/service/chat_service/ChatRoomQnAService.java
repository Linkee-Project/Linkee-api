package com.linkee.linkeeapi.chat.command.application.service.chat_service;

import com.linkee.linkeeapi.chat.command.application.dto.request.CreateQnaRequestDto;
import com.linkee.linkeeapi.chat.command.application.service.services.QnaCommandService;
import com.linkee.linkeeapi.chat.query.dto.response.QnaResponseDto;
import com.linkee.linkeeapi.chat.query.service.QnaQueryService;
import com.linkee.linkeeapi.users.command.domain.entity.User;
import com.linkee.linkeeapi.users.command.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatRoomQnAService {

    private final QnaCommandService qnaCommandService;
    private final QnaQueryService qnaQueryService;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 문제 등록 (QNA)
     */
    @Transactional
    public void registerQuestion(CreateQnaRequestDto requestDto, Long userId) {

        // 문제 생성
        qnaCommandService.createQna(requestDto, userId);

        // 🔥 출제자 닉네임 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user"));

        messagingTemplate.convertAndSend(
                "/topic/chatroom/" + requestDto.getRoomId(),
                Map.of(
                        "type", "QNA_QUESTION",
                        "question", requestDto.getQuestion(),
                        "senderNickname", user.getUserNickname()
                )
        );
    }

    /**
     * 정답 공개
     */
    @Transactional
    public QnaResponseDto revealAnswer(Long roomId) {

        List<QnaResponseDto> qnaList = qnaQueryService.getQnaListByRoomId(roomId);
        if (qnaList.isEmpty()) {
            throw new IllegalArgumentException("등록된 문제가 없습니다.");
        }

        QnaResponseDto latest = qnaList.get(qnaList.size() - 1);

        messagingTemplate.convertAndSend(
                "/topic/chatroom/" + roomId,
                Map.of(
                        "type", "QNA_ANSWER",
                        "question", latest.getQnaQuestion(),
                        "answer", latest.getQnaAnswer()
                )
        );

        return latest;
    }

    /**
     * 현재 문제 조회
     */
    @Transactional(readOnly = true)
    public QnaResponseDto getCurrentQna(Long roomId) {
        List<QnaResponseDto> list = qnaQueryService.getQnaListByRoomId(roomId);
        return list.isEmpty() ? null : list.get(list.size() - 1);
    }
}
