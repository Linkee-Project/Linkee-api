package com.linkee.linkeeapi.chat.chat_command.chat_service;

import com.linkee.linkeeapi.chat.chat_command.chat_domain.entity.ChatRoom;
import com.linkee.linkeeapi.chat.command.application.service.command_service.ChatMemberCommandServiceImpl;
import com.linkee.linkeeapi.chat.command.application.service.command_service.ChatRoomCommandService;
import com.linkee.linkeeapi.chat.command.domain.dto.command_dto.request.ChatMemberCreateRequest;
import com.linkee.linkeeapi.chat.command.domain.dto.command_dto.request.ChatRoomCreateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatRoomCreateService {

    private final ChatRoomCommandService chatRoomCommandService;
    private final ChatMemberCommandServiceImpl chatMemberCommandService;

    public ResponseEntity<?> createRoom(ChatRoomCreateRequestDto request) { // ✅ user 제거


        ChatRoom createdRoom = chatRoomCommandService.createRoom(request);

        // 챗멤버에도 추가
        ChatMemberCreateRequest memberRequest = ChatMemberCreateRequest.builder()
                .chatRoomId(createdRoom.getChatRoomId())
                .userId(request.getRoomOwnerId())
                .build();
        chatMemberCommandService.createChatMember(memberRequest);

        // ✅ 응답 JSON 구성
        Map<String, Object> response = new HashMap<>();
        response.put("chatRoomId", createdRoom.getChatRoomId());
        response.put("chatRoomName", createdRoom.getChatRoomName());
        response.put("chatRoomType", createdRoom.getChatRoomType());
        response.put("isPrivate", createdRoom.getIsPrivate());
        response.put("joinedCount", createdRoom.getJoinedCount());
        response.put("roomCapacity", createdRoom.getRoomCapacity());

        return ResponseEntity.ok(response);
    }
}

