package com.linkee.linkeeapi.chat.command.application.service.command_service;

import com.linkee.linkeeapi.chat.command.domain.dto.command_dto.request.ChatMemberCreateRequest;
import com.linkee.linkeeapi.chat.command.domain.dto.command_dto.response.ChatMemberCreateResponse;
import com.linkee.linkeeapi.chat.command.domain.dto.command_dto.response.ChatMemberDeleteResponse;
import com.linkee.linkeeapi.chat.chat_command.chat_domain.entity.ChatMember;
import com.linkee.linkeeapi.chat.chat_command.chat_repository.ChatMemberRepository;
import com.linkee.linkeeapi.chat.chat_command.chat_domain.entity.ChatRoom;
import com.linkee.linkeeapi.chat.chat_command.chat_domain.entity.ChatRoomType;
import com.linkee.linkeeapi.chat.chat_command.chat_repository.ChatRoomRepository;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user.command.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatMemberCommandServiceImpl implements ChatMemberCommandService {

    private final ChatMemberRepository chatMemberRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository jpaChatRoomRepository;

    @Override
    public ChatMemberCreateResponse createChatMember(ChatMemberCreateRequest request) {

        User foundUser = userRepository.findById(request.getUserId()).orElseThrow();
        ChatRoom foundChatRoom = jpaChatRoomRepository.findById(request.getChatRoomId()).orElseThrow();

        if (foundChatRoom.getChatRoomType() == ChatRoomType.GAME) {
            if (foundChatRoom.getJoinedCount() >= foundChatRoom.getRoomCapacity()) {
                throw new IllegalStateException("게임방 인원이 가득 찼습니다. ("
                        + foundChatRoom.getJoinedCount() + "/" + foundChatRoom.getRoomCapacity() + ")");
            }
        }

        // 중복 입장 방지
        boolean alreadyJoined = chatMemberRepository.existsByChatRoomAndUser(foundChatRoom, foundUser);
        if (alreadyJoined) {
            throw new IllegalArgumentException("이미 방에 참가한 사용자입니다.");
        }

        // 멤버 저장(입장)
        ChatMember chatMember = ChatMember.builder()
                .chatRoom(foundChatRoom)
                .user(foundUser)
                .build();

        chatMemberRepository.save(chatMember);

        // 인원수 증가
        foundChatRoom.increaseJoinedCount();
        jpaChatRoomRepository.save(foundChatRoom);

        return ChatMemberCreateResponse.builder()
                .userNickName(foundUser.getUserNickname())
                .chatRoomId(foundChatRoom.getChatRoomId())
                .chatRoomName(foundChatRoom.getChatRoomName())
                .build();

    }

    @Transactional
    @Override
    public void updateIsRead(Long chatMemberId) {
        ChatMember foundMember = chatMemberRepository.findById(chatMemberId).orElseThrow();
        foundMember.modifyIsRead();
    }

    @Transactional
    @Override
    public ChatMemberDeleteResponse deleteChatMember(Long userId, Long chatRoomId) {

        ChatRoom chatRoom = jpaChatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

        ChatMember foundMember =
                chatMemberRepository.findByChatRoomAndUser(chatRoom, userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다.")))
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자는 방 멤버가 아닙니다."));

        if (foundMember.getLeftAt() != null) {
            throw new IllegalStateException("이미 퇴장한 사용자입니다.");
        }

        foundMember.modifyLeftAt();
        chatRoom.decreaseJoinedCount();

        // 방인원 0명일시 방삭제(닫기)
        if (chatRoom.getChatRoomType() == ChatRoomType.CHAT &&
                chatRoom.getJoinedCount() <= 0) {
            chatRoom.closeRoom();
        }

        return ChatMemberDeleteResponse.builder()
                .chatRoomId(chatRoom.getChatRoomId())
                .userNickName(foundMember.getUser().getUserNickname())
                .build();
    }

}
