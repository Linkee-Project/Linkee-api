package com.linkee.linkeeapi.chat.chat_command.chat_service;

import com.linkee.linkeeapi.chat.chat_command.chat_domain.dto.ChatMessageDto;
import com.linkee.linkeeapi.chat.chat_command.chat_domain.entity.ChatMember;
import com.linkee.linkeeapi.chat.chat_command.chat_domain.entity.ChatRoom;
import com.linkee.linkeeapi.chat.chat_command.chat_repository.ChatMemberRepository;
import com.linkee.linkeeapi.chat.chat_command.chat_repository.ChatRoomRepository;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.common.security.jwt.JwtTokenProvider;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user.command.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomInOutService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final SimpMessagingTemplate messagingTemplate;

    private User getUserFromToken(String token) {
        if (token.startsWith("Bearer ")) token = token.substring(7);

        if (!jwtTokenProvider.validateToken(token)) {
            throw new BusinessException(ErrorCode.REPORT_NO_ACCESS, "로그인 정보 없음");
        }

        String email = jwtTokenProvider.getUsername(token);
        return userRepository.findByUserEmail(email).orElseThrow(() -> new BusinessException(ErrorCode.INVALID_USER_ID));
    }

    @Transactional
    public ChatMessageDto joinRoom(Long roomId, String token, Integer inputRoomCode) {
        User user = getUserFromToken(token);
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        System.out.println("++++++++++++++++  " + room.getIsPrivate());
        System.out.println("++++++++++++++++  " + inputRoomCode);

        // 비밀방 검증
        if (Status.Y.equals(room.getIsPrivate())) {
            if (room.getRoomCode() == null || !room.getRoomCode().equals(inputRoomCode)) {
                // 실패 메시지 전송
                ChatMessageDto failMsg = ChatMessageDto.builder()
                        .roomId(roomId)
                        .message("비밀번호가 틀렸습니다.")
                        .senderNickname("SYSTEM")
                        .sentAt(LocalDateTime.now())
                        .build();
                messagingTemplate.convertAndSend("/topic/chatroom/" + roomId, failMsg);

                // 입장 금지
                throw new RuntimeException("비밀번호가 틀렸습니다.");
            }
        }

        boolean alreadyJoined = chatMemberRepository.existsByChatRoomAndUser(room, user);
        if (alreadyJoined) {
            ChatMember cm = chatMemberRepository.findByChatRoomAndUser(room, user).orElseThrow();
            cm.setJoinedAt(LocalDateTime.now());
            cm.modifyIsRead();
        } else {
            ChatMember newMember = ChatMember.builder().chatRoom(room).user(user).build();
            chatMemberRepository.save(newMember);
            room.increaseJoinedCount();
            chatRoomRepository.save(room);
        }

        return ChatMessageDto.builder()
                .roomId(roomId)
                .message(user.getUserNickname() + "님이 입장했습니다.")
                .senderNickname("SYSTEM")
                .sentAt(LocalDateTime.now())
                .build();
    }

    @Transactional
    public ChatMessageDto leaveRoom(Long roomId, String token) {
        User user = getUserFromToken(token);
        ChatRoom room = chatRoomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));

        boolean isOwner = room.getRoomOwner().getUserId().equals(user.getUserId());

        if (isOwner) {
            List<ChatMember> members = chatMemberRepository.findByChatRoom(room);
            members.forEach(ChatMember::modifyLeftAt);
            chatMemberRepository.deleteAll(members);
            chatRoomRepository.deleteById(roomId);
        } else {
            ChatMember member = chatMemberRepository.findByChatRoomAndUser(room, user).orElseThrow();
            chatMemberRepository.delete(member);
            room.decreaseJoinedCount();
            if (room.getJoinedCount() == 0) chatRoomRepository.deleteById(roomId);
            else chatRoomRepository.save(room);
        }

        return ChatMessageDto.builder()
                .roomId(roomId)
                .message(user.getUserNickname() + "님이 퇴장했습니다.")
                .senderNickname("SYSTEM")
                .sentAt(LocalDateTime.now())
                .build();
    }
}
