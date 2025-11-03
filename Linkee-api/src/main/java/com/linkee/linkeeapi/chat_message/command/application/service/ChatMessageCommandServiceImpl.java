package com.linkee.linkeeapi.chat_message.command.application.service;

import com.linkee.linkeeapi.chat_member.command.domain.aggregate.entity.ChatMember;
import com.linkee.linkeeapi.chat_member.command.infrastructure.repository.ChatMemberRepository;
import com.linkee.linkeeapi.chat_message.command.application.dto.request.ChatMessageCreateRequest;
import com.linkee.linkeeapi.chat_message.command.application.dto.request.MarkAsReadRequest;
import com.linkee.linkeeapi.chat_message.command.domain.aggregate.entity.ChatMessage;
import com.linkee.linkeeapi.chat_message.command.infrastructure.repository.ChatMessageRepository;
import com.linkee.linkeeapi.chat_room.command.domain.aggregate.ChatRoom;
import com.linkee.linkeeapi.chat_room.command.infrastructure.repository.JpaChatRoomRepository;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user.command.application.service.util.UserFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageCommandServiceImpl implements ChatMessageCommandService{

    private final ChatMessageRepository chatMessageRepository;
    private final UserFinder userFinder;
    private final JpaChatRoomRepository chatRoomRepository;
    private final ChatMemberRepository chatMemberRepository;

    /*
     * 메시지 전송 시 호출
     * 모든 멤버의 isRead = N으로 초기화 (보낸 사람 제외)
     */

    @Override
    public void createChatMessage(ChatMessageCreateRequest request) {

        User foundUser = userFinder.getById(request.getSenderId());
        ChatRoom foundRoom = chatRoomRepository.findById(request.getChatRoomId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

        ChatMessage message = ChatMessage.builder()
                .messageContent(request.getMessageContent())
                .chatRoom(foundRoom)
                .sender(foundUser)
                .build();

        chatMessageRepository.save(message);

        //모든 멤버의 isRead=N //새로운 메세지가 등록되었으므로
        List<ChatMember> members = chatMemberRepository.findAllByChatRoom(foundRoom);
        for (ChatMember member : members) {
            if(!member.getUser().getUserId().equals(request.getSenderId())) {
                member.setIsRead(Status.N);
                chatMemberRepository.save(member);
            }
        }
    }

    /*
     * 사용자가 채팅방 열 때 호출
     * 해당 멤버의 isRead = Y 처리
     */
    @Override
    public void markChatRoomAsRead(MarkAsReadRequest request) {
        ChatRoom chatRoom = chatRoomRepository.findById(request.getChatRoomId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방"));

        User user = userFinder.getById(request.getUserId());

        ChatMember chatMember = chatMemberRepository.findByChatRoomAndUser(chatRoom, user)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자는 채팅방 멤버가 아닙니다."));

        chatMember.modifyIsRead();
        chatMemberRepository.save(chatMember);
    }
}
