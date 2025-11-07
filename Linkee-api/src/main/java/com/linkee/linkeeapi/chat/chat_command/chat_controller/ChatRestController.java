package com.linkee.linkeeapi.chat.chat_command.chat_controller;


import com.linkee.linkeeapi.chat.chat_command.chat_domain.dto.response.ChatRoomResponseDto;
import com.linkee.linkeeapi.chat.chat_command.chat_domain.entity.ChatRoom;
import com.linkee.linkeeapi.chat.chat_command.chat_repository.ChatMessageMongoRepository;
import com.linkee.linkeeapi.chat.chat_command.chat_repository.ChatRoomRepository;
import com.linkee.linkeeapi.chat.chat_command.chat_service.ChatRoomCreateService;
import com.linkee.linkeeapi.chat.command.application.service.command_service.ChatRoomCommandService;
import com.linkee.linkeeapi.chat.command.domain.dto.command_dto.request.ChatRoomCreateRequestDto;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.common.security.jwt.JwtTokenProvider;
import com.linkee.linkeeapi.common.security.model.CustomUser;
import com.linkee.linkeeapi.common.security.service.CustomUserDetails;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user.command.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRestController {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageMongoRepository chatMessageMongoRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ChatRoomCommandService chatRoomCommandService;
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



}
