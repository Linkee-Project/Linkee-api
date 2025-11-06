package com.linkee.linkeeapi.chat_room.command.application.controller;

import com.linkee.linkeeapi.chat_member.command.domain.aggregate.entity.ChatMember;
import com.linkee.linkeeapi.chat_member.command.infrastructure.repository.ChatMemberRepository;
import com.linkee.linkeeapi.chat_room.command.application.dto.request.ChatRoomCreateRequestDto;
import com.linkee.linkeeapi.chat_room.command.application.dto.request.ChatRoomDeleteRequestDto;
import com.linkee.linkeeapi.chat_room.command.application.service.ChatRoomCommandServiceImpl;
import com.linkee.linkeeapi.chat_room.command.domain.aggregate.ChatRoom;
import com.linkee.linkeeapi.chat_room.command.domain.aggregate.ChatRoomType;
import com.linkee.linkeeapi.chat_room.command.infrastructure.repository.JpaChatRoomRepository;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.user.command.application.service.util.UserFinder;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user.command.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

class ChatRoomCommandServiceTest {

    @Mock
    private JpaChatRoomRepository chatRoomRepository;

    @Mock
    private UserRepository userRepository; // ✅ 추가

    @Mock
    private ChatMemberRepository chatMemberRepository; // ✅ 추가
    @Mock
    private UserFinder userFinder; // ✅ 이거 추가!


    @InjectMocks
    private ChatRoomCommandServiceImpl chatRoomCommandService;


    public ChatRoomCommandServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("✅ 채팅방 생성 성공 테스트")
    void createRoom_Success() {
        // given
        ChatRoomCreateRequestDto request = ChatRoomCreateRequestDto.builder()
                .chatRoomName("테스트 방")
                .chatRoomType(ChatRoomType.CHAT)
                .isPrivate(Status.N)
                .roomCode(1111)
                .roomOwnerId(1L)
                .roomCapacity(5)
                .build();

        User owner = User.builder()
                .userId(1L)
                .userNickname("테스터")
                .build();

        ChatRoom chatRoom = ChatRoom.builder()
                .chatRoomName(request.getChatRoomName())
                .chatRoomType(request.getChatRoomType())
                .isPrivate(request.getIsPrivate())
                .roomCode(request.getRoomCode())
                .roomOwner(owner)
                .roomCapacity(request.getRoomCapacity())
                .build();

        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(chatRoom);

        // when
        chatRoomCommandService.createRoom(request);

        // then
        verify(chatRoomRepository, times(1)).save(any(ChatRoom.class));
    }

    @Test
    @DisplayName("✅ 게임방 삭제 성공 테스트")
    void deleteGameRoom_Success() {
        // given
        ChatRoomDeleteRequestDto request = new ChatRoomDeleteRequestDto(1L, 10L);
        ChatRoom room = ChatRoom.builder()
                .chatRoomId(1L)
                .chatRoomName("게임방1")
                .chatRoomType(ChatRoomType.GAME)
                .isPrivate(Status.N)
                .roomOwner(User.builder().userId(10L).build())
                .joinedCount(3)
                .roomCapacity(5)
                .roomStatus(Status.Y)
                .build();

        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(room));

        // when
        chatRoomCommandService.deleteGameRoom(request);

        // then
        verify(chatRoomRepository, times(1)).findById(1L);
        assertThat(room.getRoomStatus()).isEqualTo(Status.N);
    }

    @Test
    @DisplayName("✅ 채팅방 나가기 성공 테스트")
    void leaveRoom_Success() {
        // given
        ChatRoomDeleteRequestDto request = new ChatRoomDeleteRequestDto(1L, 5L);
        ChatRoom room = ChatRoom.builder()
                .chatRoomId(1L)
                .chatRoomName("자율방 테스트")
                .chatRoomType(ChatRoomType.CHAT)
                .isPrivate(Status.N)
                .roomOwner(User.builder().userId(10L).build())
                .joinedCount(2)
                .roomCapacity(5)
                .roomStatus(Status.Y)
                .build();

        User user = User.builder().userId(5L).build();

        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(userFinder.getById(5L)).thenReturn(user);
        when(chatMemberRepository.findByChatRoomAndUser(room, user))
                .thenReturn(Optional.of(mock(ChatMember.class))); // ✅ 추가

        // when
        chatRoomCommandService.leaveRoom(request);

        // then
        verify(chatRoomRepository, times(1)).findById(1L);
        verify(chatMemberRepository, times(1)).findByChatRoomAndUser(room, user);
        assertThat(room.getJoinedCount()).isEqualTo(1);
        if (room.getJoinedCount() <= 0)
            assertThat(room.getRoomStatus()).isEqualTo(Status.N);
    }


}
