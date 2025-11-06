package com.linkee.linkeeapi.chat_member.query.controller;

import com.linkee.linkeeapi.chat_member.query.dto.request.ChatMemberSearchRequest;
import com.linkee.linkeeapi.chat_room.command.domain.aggregate.ChatRoom;
import com.linkee.linkeeapi.chat_room.command.domain.aggregate.ChatRoomType;
import com.linkee.linkeeapi.chat_room.command.infrastructure.repository.JpaChatRoomRepository;
import com.linkee.linkeeapi.common.enums.RoomStatus;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user.command.infrastructure.repository.UserRepository;
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
class ChatMemberQueryControllerTest {

    @Autowired
    private ChatMemberQueryController controller;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JpaChatRoomRepository chatRoomRepository;

    @Test
    @DisplayName("채팅 멤버 전체 리스트 조회 테스트")
    void selectAllChatMember() {
        // given : 유저와 채팅방 생성
        User user = userRepository.save(User.createNormalUser(
                "member@test.com",
                "password123",
                "홍길동"
        ));

        ChatRoom room = chatRoomRepository.save(ChatRoom.builder()
                .chatRoomName("테스트방")
                .chatRoomType(ChatRoomType.CHAT)
                .roomCapacity(5)
                .roomCode(9999)
                .roomOwner(user)
                .build());


        // ChatMemberSearchRequest 생성
        ChatMemberSearchRequest request = ChatMemberSearchRequest.builder()
                .keyword("홍길동")
                .page(0)
                .size(10)
                .offset(0)
                .chatRoomId(room.getChatRoomId())
                .userId(user.getUserId())
                .build();

        // when
        var response = controller.selectAllChatMember(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getCurrentPage()).isEqualTo(0);
        assertThat(response.getSize()).isGreaterThanOrEqualTo(0);
        assertThat(response.getContent()).isNotNull();

    }
}
