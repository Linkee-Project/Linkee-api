package com.linkee.linkeeapi.chat.query.service;

import com.linkee.linkeeapi.chat.command.domain.entity.ChatMessage;
import com.linkee.linkeeapi.chat.command.domain.repository.ChatMessageRepository;
import com.linkee.linkeeapi.chat.command.domain.dto.query_dto.request.ChatMessageSearchRequest;
import com.linkee.linkeeapi.chat.command.domain.dto.query_dto.response.ChatMessageResponse;
import com.linkee.linkeeapi.chat.chat_command.chat_domain.entity.ChatRoom;
import com.linkee.linkeeapi.chat.chat_command.chat_domain.entity.ChatRoomType;
import com.linkee.linkeeapi.chat.command.application.service.query_serivce.ChatMessageQueryService;
import com.linkee.linkeeapi.common.enums.Role;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ChatMessageQueryServiceImplTest {

    @Autowired
    private ChatMessageRepository repository;
    @Autowired
    private ChatMessageQueryService chatMessageQueryService;

    @Test
    @DisplayName("채팅메세지 생성")
    void createMessage(){
        User user1 = User.createNormalUser("user01", "pass01", "배짱이");

        ChatRoom room = ChatRoom.builder()
                .chatRoomName("채팅방1")
                .chatRoomType(ChatRoomType.GAME)
                .roomOwner(user1)
                .build();

        ChatMessage message = ChatMessage.builder()
                .messageContent("메세지내용")
                .chatRoom(room)
                .sender(user1)
                .build();

        ChatMessage result = repository.save(message);

        assertThat(result.getMessageContent()).isEqualTo("메세지내용");

    }

    @Test
    @DisplayName("전체 채팅메세지 조회")
    void selectAll(){
        User user1 = User.createNormalUser( "user01", "pass01", "배짱이");

        ChatRoom room = ChatRoom.builder()
                .chatRoomName("채팅방1")
                .chatRoomType(ChatRoomType.GAME)
                .roomOwner(user1)
                .build();


        for(int i = 1; i < 6 ; i++) {
            ChatMessage message = ChatMessage.builder()
                    .messageContent("메세지내용" + i)
                    .chatRoom(room)
                    .sender(user1)
                    .build();

            repository.save(message);
        }


        PageResponse<ChatMessageResponse> response = chatMessageQueryService.selectAllChatMessage(new ChatMessageSearchRequest());


        String result = response.getContent().get(0).getMessageContent();

        assertThat(result).isEqualTo("메세지내용5");

    }

}