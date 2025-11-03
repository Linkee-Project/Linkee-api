package com.linkee.linkeeapi.chat_member.command.application.service;

import com.linkee.linkeeapi.chat_member.command.application.dto.reqeust.ChatMemberCreateRequest;
import com.linkee.linkeeapi.chat_member.command.application.dto.response.ChatMemberCreateResponse;
import com.linkee.linkeeapi.chat_member.command.domain.aggregate.entity.ChatMember;
import com.linkee.linkeeapi.chat_member.command.infrastructure.repository.ChatMemberRepository;
import com.linkee.linkeeapi.chat_room.command.domain.aggregate.ChatRoom;
import com.linkee.linkeeapi.chat_room.command.infrastructure.repository.JpaChatRoomRepository;
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
    private final JpaChatRoomRepository  jpaChatRoomRepository;

    @Override
    public ChatMemberCreateResponse createChatMember(ChatMemberCreateRequest request) {

        User foundUser = userRepository.findById(request.getUserId()).orElseThrow();
        ChatRoom foundChatRoom = jpaChatRoomRepository.findById(request.getChatRoomId()).orElseThrow();

        ChatMember chatMember = ChatMember.builder()
                .chatRoom(foundChatRoom)
                .user(foundUser)
                .build();

        chatMemberRepository.save(chatMember);

        ChatMemberCreateResponse response = ChatMemberCreateResponse.builder()
                .userNickName(foundUser.getUserNickname())
                .chatRoomId(foundChatRoom.getChatRoomId())
                .chatRoomName(foundChatRoom.getChatRoomName())
                .build();

        return response;

    }

    @Transactional
    @Override
    public void updateIsRead(Long chatMemberId) {
        ChatMember foundMember = chatMemberRepository.findById(chatMemberId).orElseThrow();
        foundMember.modifyIsRead();
    }

    @Transactional
    @Override
    public String deleteChatMember(Long chatMemberId) {

        ChatMember foundMember = chatMemberRepository.findById(chatMemberId).orElseThrow();

        String userNickName = userRepository.findById(foundMember.getUser().getUserId()).orElseThrow().getUserNickname();

        foundMember.modifyLeftAt();


        return userNickName;
    }


}
