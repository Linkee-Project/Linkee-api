package com.linkee.linkeeapi.chat.chat_command.chat_controller;


import com.linkee.linkeeapi.chat.chat_command.chat_domain.entity.ChatRoom;
import com.linkee.linkeeapi.chat.chat_command.chat_repository.ChatMessageMongoRepository;
import com.linkee.linkeeapi.chat.chat_command.chat_repository.ChatRoomRepository;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.common.security.jwt.JwtTokenProvider;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user.command.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRestController {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageMongoRepository chatMessageMongoRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // 전체 방 조회
    @GetMapping("/rooms")
    public ResponseEntity<?> getAllRooms(@RequestHeader("Authorization") String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        return ResponseEntity.ok(chatRoomRepository.findAll());
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
    public ResponseEntity<?> createRoom(@RequestHeader("Authorization") String token,
                                        @RequestParam String roomName) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new BusinessException(ErrorCode.CHAT_ROOM_NOT_FOUND);
        }

        String userEmail = jwtTokenProvider.getUsername(token);
        User owner = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_USER_ID));

        ChatRoom room = ChatRoom.builder()
                .chatRoomName(roomName)
                .roomOwner(owner)
                .joinedCount(1)
                .roomStatus(Status.Y)
                .build();

        chatRoomRepository.save(room);

        return ResponseEntity.ok(room);
    }
}
