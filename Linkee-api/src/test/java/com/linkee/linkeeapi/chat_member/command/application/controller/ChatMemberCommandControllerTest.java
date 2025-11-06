package com.linkee.linkeeapi.chat_member.command.application.controller;

import com.linkee.linkeeapi.chat_member.command.application.dto.reqeust.ChatMemberCreateRequest;
import com.linkee.linkeeapi.chat_room.command.domain.aggregate.ChatRoom;
import com.linkee.linkeeapi.chat_room.command.domain.aggregate.ChatRoomType;
import com.linkee.linkeeapi.chat_room.command.infrastructure.repository.JpaChatRoomRepository;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user.command.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ChatMemberCommandControllerTest {

    @Autowired
    private ChatMemberCommandController controller;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JpaChatRoomRepository chatRoomRepository;


    @Test
    @DisplayName("✅ 채팅 멤버 생성 테스트")
    void createChatMember() {
        // given : 사용자 생성
        User user = userRepository.save(User.createNormalUser(
                "test@test.com",
                "password123",
                "홍길동"
        ));

        // ✅ 채팅방 생성 (User 연관관계 매핑 필수)
        ChatRoom chatRoom = ChatRoom.builder()
                .chatRoomName("테스트방")
                .chatRoomType(ChatRoomType.CHAT)
                .isPrivate(Status.N)
                .roomCode(1234)
                .roomOwner(user)
                .joinedCount(1)
                .roomCapacity(5)
                .roomStatus(Status.Y)
                .build();
        chatRoomRepository.save(chatRoom);

        ChatMemberCreateRequest request = ChatMemberCreateRequest.builder()
                .chatRoomId(chatRoom.getChatRoomId())
                .userId(user.getUserId())
                .build();

        // when
        ResponseEntity<String> response = controller.createChatMember(request);

        // then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("홍길동", "입장하셨습니다");
    }

    @Test
    @DisplayName("✅ 읽음 상태 업데이트 테스트")
    void updateIsRead() {
        // given : 사용자 & 채팅방 & 멤버 생성
        User user = userRepository.save(User.createNormalUser(
                "read@test.com",
                "password123",
                "독자"
        ));

        ChatRoom chatRoom = ChatRoom.builder()
                .chatRoomName("읽기방")
                .chatRoomType(ChatRoomType.CHAT)
                .isPrivate(Status.N)
                .roomCode(5678)
                .roomOwner(user)
                .joinedCount(1)
                .roomCapacity(5)
                .roomStatus(Status.Y)
                .build();
        chatRoomRepository.save(chatRoom);

        ChatMemberCreateRequest request = ChatMemberCreateRequest.builder()
                .chatRoomId(chatRoom.getChatRoomId())
                .userId(user.getUserId())
                .build();
        controller.createChatMember(request);

        Long chatMemberId = 1L; // 첫 번째 ChatMember ID (H2에서는 기본 1부터 시작)

        // when
        ResponseEntity<String> response = controller.updateIsRead(chatMemberId);

        // then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo("읽음 처리 완료");
    }

    @Test
    @DisplayName("채팅방 퇴장 테스트")
    void leaveRoom() {
        // given : 사용자 및 채팅방 생성
        User user = userRepository.save(User.createNormalUser(
                "leave@test.com",
                "password123",
                "테스터"
        ));

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder()
                .chatRoomName("테스트방")
                .chatRoomType(ChatRoomType.CHAT)
                .isPrivate(Status.N)
                .roomCode(1234)
                .roomOwner(user)
                .joinedCount(1)
                .roomCapacity(5)
                .roomStatus(Status.Y)
                .build());

        // ✅ 사용자 채팅방 멤버로 등록
        ChatMemberCreateRequest joinRequest = ChatMemberCreateRequest.builder()
                .chatRoomId(chatRoom.getChatRoomId())
                .userId(user.getUserId())
                .build();
        controller.createChatMember(joinRequest);

        // when : 방 퇴장 요청
        ResponseEntity<String> response = controller.leaveRoom(user.getUserId(), chatRoom.getChatRoomId());

        // then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("퇴장");
    }

}
