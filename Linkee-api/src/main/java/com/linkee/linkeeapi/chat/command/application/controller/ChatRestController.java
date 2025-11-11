package com.linkee.linkeeapi.chat.command.application.controller;


import com.linkee.linkeeapi.chat.command.application.dto.request.ChatRoomCreateRequestDto;
import com.linkee.linkeeapi.chat.command.application.dto.request.ChatRoomJoinRequestDto;
import com.linkee.linkeeapi.chat.command.application.dto.response.ChatMemberDto;
import com.linkee.linkeeapi.chat.command.application.dto.response.ChatRoomJoinResponseDto;
import com.linkee.linkeeapi.chat.command.application.dto.response.ChatRoomResponseDto;
import com.linkee.linkeeapi.chat.command.application.service.chat_service.ChatRoomCreateService;
import com.linkee.linkeeapi.chat.command.application.service.chat_service.ChatRoomInOutService;
import com.linkee.linkeeapi.chat.command.instructure.repository.ChatMessageMongoRepository;
import com.linkee.linkeeapi.chat.command.instructure.repository.ChatRoomRepository;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.common.config.jwt.JwtTokenProvider;
import com.linkee.linkeeapi.users.command.domain.entity.User;
import com.linkee.linkeeapi.users.command.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRestController {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageMongoRepository chatMessageMongoRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ChatRoomInOutService chatRoomInOutService;
    private final ChatRoomCreateService chatRoomCreateService;

    // 전체 방 조회
    @GetMapping("/rooms")
    public ResponseEntity<?> getAllRooms(@RequestHeader("Authorization") String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        List<ChatRoomResponseDto> rooms = chatRoomRepository.findAll().stream()
                .map(room -> new ChatRoomResponseDto(
                        room.getChatRoomId(),
                        room.getChatRoomName(),
                        room.getChatRoomType(),
                        room.getIsPrivate(),
                        room.getJoinedCount()
                ))
                .toList();

        return ResponseEntity.ok(rooms);
    }

    // 특정 방 메시지 조회
    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<?> getRoomMessages(@PathVariable Long roomId,
                                             @RequestHeader("Authorization") String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new BusinessException(ErrorCode.CHAT_ROOM_NOT_FOUND);
        }
        return ResponseEntity.ok(chatMessageMongoRepository.findAllByRoomIdOrderBySentAtAsc(roomId));
    }

    // 새 방 만들기
    @PostMapping("/rooms")
    public ResponseEntity<?> createRoom(
            @RequestHeader("Authorization") String token,
            @RequestBody ChatRoomCreateRequestDto request) {

        String pureToken = token.replace("Bearer ", "");
        String userEmail = jwtTokenProvider.getUsername(pureToken); // ✅ 이메일 꺼내기

        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_USER_ID));

        request.setRoomOwnerId(user.getUserId()); // ✅ DB에서 찾은 ID 주입

        return chatRoomCreateService.createRoom(request);
    }



    // 방입장 ( 비밀번호있을시 검증 완료됐을때만 구독 )
    @PostMapping("/rooms/{roomId}/join")
    public ResponseEntity<?> joinRoom(
            @PathVariable Long roomId,
            @RequestHeader("Authorization") String token,
            @RequestBody(required = false) ChatRoomJoinRequestDto request) {

        Integer roomCode = request != null ? request.getRoomCode() : null;

        try {
            var joinMessage = chatRoomInOutService.joinRoom(roomId, token, roomCode);
            return ResponseEntity.ok(
                    new ChatRoomJoinResponseDto(roomId, joinMessage.getMessage())
            );
        } catch (RuntimeException ex) {
            return ResponseEntity.status(401).body(ex.getMessage());
        }
    }


    @GetMapping("/{roomId}/members")
    public ResponseEntity<List<ChatMemberDto>> getRoomMembers(@PathVariable Long roomId) {
        List<ChatMemberDto> members = chatRoomInOutService.getRoomMembers(roomId);
        return ResponseEntity.ok(members);
    }
}