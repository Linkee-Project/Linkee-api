package com.linkee.linkeeapi.chat.chat_command.chat_controller;

import com.linkee.linkeeapi.chat.chat_command.chat_service.ChatRoomQnAService;
import com.linkee.linkeeapi.qna.command.application.dto.request.CreateQnaRequestDto;
import com.linkee.linkeeapi.qna.query.dto.response.QnaResponseDto;
import com.linkee.linkeeapi.common.security.jwt.JwtTokenProvider;
import com.linkee.linkeeapi.user.command.application.service.util.UserFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat/qna")
@RequiredArgsConstructor
public class ChatQnAController {

    private final ChatRoomQnAService chatRoomQnAService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserFinder userFinder;

    // 문제 등록
    @PostMapping("/{roomId}")
    public ResponseEntity<?> createQna(
            @PathVariable Long roomId,
            @RequestHeader("Authorization") String token,
            @RequestBody CreateQnaRequestDto requestDto) {

        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        // ✅ 토큰에서 이메일 추출
        token = token.replace("Bearer ", "");
        String userEmail = jwtTokenProvider.getUsername(token);

        // ✅ 이메일로 userId 조회
        Long userId = userFinder.getByEmail(userEmail).getUserId();

        // ✅ 방 ID 설정
        requestDto.setRoomId(roomId);

        // ✅ 문제 등록 (userId 전달)
        chatRoomQnAService.registerQuestion(requestDto, userId);

        return ResponseEntity.ok("문제 등록 완료");
    }


    // 답 공개
    @GetMapping("/{roomId}/reveal")
    public ResponseEntity<?> revealAnswer(
            @PathVariable Long roomId,
            @RequestHeader("Authorization") String token) {

        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        QnaResponseDto qna = chatRoomQnAService.revealAnswer(roomId);
        return ResponseEntity.ok(qna);
    }

    // 현재 문제 조회
    @GetMapping("/{roomId}/current")
    public ResponseEntity<?> getCurrentQna(
            @PathVariable Long roomId,
            @RequestHeader("Authorization") String token) {

        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        QnaResponseDto qna = chatRoomQnAService.getCurrentQna(roomId);
        return ResponseEntity.ok(qna);
    }
}
