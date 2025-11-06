package com.linkee.linkeeapi.chat.chat_command.chat_service;

import com.linkee.linkeeapi.chat.chat_command.chat_domain.dto.ChatMemberDto;
import com.linkee.linkeeapi.chat.chat_command.chat_domain.entity.ChatMember;
import com.linkee.linkeeapi.chat.chat_command.chat_domain.entity.ChatMessageMongo;
import com.linkee.linkeeapi.chat.chat_command.chat_domain.entity.ChatRoom;
import com.linkee.linkeeapi.chat.chat_command.chat_repository.ChatMemberRepository;
import com.linkee.linkeeapi.chat.chat_command.chat_repository.ChatMessageMongoRepository;
import com.linkee.linkeeapi.chat.chat_command.chat_repository.ChatRoomRepository;
import com.linkee.linkeeapi.common.security.jwt.JwtTokenProvider;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user.command.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final ChatMessageMongoRepository chatMessageMongoRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /** 토큰 유효성 체크 */
    public boolean isValidToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    /** 토큰으로 닉네임 조회 */
    public String getNicknameFromToken(String token) {
        String email = jwtTokenProvider.getUsername(token);
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getUserNickname();
    }

    /** 전체 채팅방 조회 */
    public List<ChatRoom> getAllRooms() {
        return chatRoomRepository.findAll();
    }

    /** 방 메시지 조회 */
    @Transactional(readOnly = true)
    public List<ChatMessageMongo> getMessages(Long roomId) {
        return chatMessageMongoRepository.findByRoomIdOrderBySentAtAsc(roomId);
    }

    /** 메시지 저장 + 반환 */
    @Transactional
    public ChatMessageMongo sendMessage(Long roomId, String nickname, String message) {
        ChatMessageMongo msg = ChatMessageMongo.builder()
                .roomId(roomId)
                .senderNickname(nickname)
                .message(message)
                .sentAt(LocalDateTime.now())
                .build();
        return chatMessageMongoRepository.save(msg);
    }

    /** 방 입장 */
    @Transactional
    public ChatMemberDto joinRoomWithToken(Long roomId, String token) {
        String email = jwtTokenProvider.getUsername(token);
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        ChatMember member = chatMemberRepository.findByChatRoomAndUser(room, user).orElse(null);
        if (member != null) {
            member.setJoinedAt(LocalDateTime.now());
            member.setLeftAt(null);
            return toDto(member);
        }

        ChatMember newMember = ChatMember.builder()
                .chatRoom(room)
                .user(user)
                .joinedAt(LocalDateTime.now())
                .build();

        room.increaseJoinedCount();
        chatRoomRepository.save(room);
        return toDto(chatMemberRepository.save(newMember));
    }

    /** 방 퇴장 */
    @Transactional
    public ChatMemberDto leaveRoomWithToken(Long roomId, String token) {
        String email = jwtTokenProvider.getUsername(token);
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        ChatMember member = chatMemberRepository.findByChatRoomAndUser(room, user)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        member.setLeftAt(LocalDateTime.now());
        chatMemberRepository.save(member);

        room.decreaseJoinedCount();
        chatRoomRepository.save(room);

        return toDto(member);
    }

    /** DTO 변환 */
    private ChatMemberDto toDto(ChatMember member) {
        return new ChatMemberDto(
                member.getChatMemberId(),
                member.getUser().getUserId(),
                member.getUser().getUserNickname()
        );
    }
}
