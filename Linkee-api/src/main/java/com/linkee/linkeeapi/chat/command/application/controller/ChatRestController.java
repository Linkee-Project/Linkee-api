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
import com.linkee.linkeeapi.chat.query.dto.request.ChatRoomListRequestDto;
import com.linkee.linkeeapi.chat.query.dto.request.GameRoomListRequestDto;
import com.linkee.linkeeapi.chat.query.service.ChatRoomQueryService;
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
@RequestMapping("/api/v1/chat/rooms")
@RequiredArgsConstructor
public class ChatRestController {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageMongoRepository chatMessageMongoRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ChatRoomInOutService chatRoomInOutService;
    private final ChatRoomCreateService chatRoomCreateService;

    //유한세가 추가함
    //내 채팅방 조회
    private final ChatRoomQueryService chatRoomQueryService;
    @GetMapping("/chat/my")
    public ResponseEntity<?> getMyChatRooms(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        String pureToken = token.replace("Bearer ", "").trim();

        if (!jwtTokenProvider.validateToken(pureToken)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String userEmail = jwtTokenProvider.getUsername(pureToken);


        Long userId = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_USER_ID))
                .getUserId();

        ChatRoomListRequestDto request = new ChatRoomListRequestDto(userId, page, size);

        return ResponseEntity.ok(chatRoomQueryService.getMyChatRoomList(request));
    }

    //유한세가 추가함
    // 전체 게임방 목록 조회
    @GetMapping("/game")
    public ResponseEntity<?> getGameRooms(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        String pureToken = token.replace("Bearer ", "").trim();

        if (!jwtTokenProvider.validateToken(pureToken)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        GameRoomListRequestDto request = new GameRoomListRequestDto(page, size);

        // PageResponse<GameRoomListResponseDto> 그대로 반환
        return ResponseEntity.ok(
                chatRoomQueryService.getGameRoomList(request)
        );
    }



    // 전체 방 조회
    @GetMapping
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
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<?> getRoomMessages(@PathVariable Long roomId,
                                             @RequestHeader("Authorization") String token) {
        String pureToken = token.replace("Bearer ", "").trim();

        if (!jwtTokenProvider.validateToken(pureToken)) {
            throw new BusinessException(ErrorCode.CHAT_ROOM_NOT_FOUND ,"유효하지 않은 토큰입니다");
        }
        return ResponseEntity.ok(chatMessageMongoRepository.findAllByRoomIdOrderBySentAtAsc(roomId));
    }

    // 새 방 만들기
    @PostMapping
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
    @PostMapping("/{roomId}/join")
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

    // 방멤버조회
    @GetMapping("/{roomId}/members")
    public ResponseEntity<List<ChatMemberDto>> getRoomMembers(@PathVariable Long roomId) {
        List<ChatMemberDto> members = chatRoomInOutService.getRoomMembers(roomId);
        return ResponseEntity.ok(members);
    }

}