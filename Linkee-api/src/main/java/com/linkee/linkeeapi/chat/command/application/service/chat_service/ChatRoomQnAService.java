package com.linkee.linkeeapi.chat.command.application.service.chat_service;

import com.linkee.linkeeapi.chat.command.application.dto.request.CreateQnaRequestDto;
import com.linkee.linkeeapi.chat.command.application.service.services.QnaCommandService;
import com.linkee.linkeeapi.chat.query.dto.response.QnaResponseDto;
import com.linkee.linkeeapi.chat.query.service.QnaQueryService;
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
    private final QnaQueryService  qnaQueryService;
    private final SimpMessagingTemplate messagingTemplate;


    /**
     * 게임방 내에서 멤버가 문제와 답을 등록
     */
    public void registerQuestion(CreateQnaRequestDto requestDto, Long userId) {
        qnaCommandService.createQna(requestDto, userId); // ✅ userId 전달 추가

        messagingTemplate.convertAndSend(
                "/topic/chatroom/" + requestDto.getRoomId(),
                Map.of(
                        "type", "QNA_QUESTION",
                        "question", requestDto.getQuestion()
                )
        );
    }


    /**
     * 답 공개
     */
    public QnaResponseDto revealAnswer(Long roomId) {
        List<QnaResponseDto> qnaList = qnaQueryService.getQnaListByRoomId(roomId);
        if (qnaList.isEmpty()) {
            throw new IllegalArgumentException("등록된 문제가 없습니다.");
        }

        QnaResponseDto latestQna = qnaList.get(qnaList.size() - 1);

        // 🔹 채팅방 구독자들에게 답 공개
        messagingTemplate.convertAndSend(
                "/topic/chatroom/" + roomId,
                Map.of(
                        "type", "QNA_ANSWER",
                        "question", latestQna.getQnaQuestion(),
                        "answer", latestQna.getQnaAnswer()
                )
        );

        return latestQna;
    }

    /**
     * 현재 문제 조회
     */
    @Transactional(readOnly = true)
    public QnaResponseDto getCurrentQna(Long roomId) {
        List<QnaResponseDto> qnaList = qnaQueryService.getQnaListByRoomId(roomId);
        return qnaList.isEmpty() ? null : qnaList.get(qnaList.size() - 1);
    }

}
