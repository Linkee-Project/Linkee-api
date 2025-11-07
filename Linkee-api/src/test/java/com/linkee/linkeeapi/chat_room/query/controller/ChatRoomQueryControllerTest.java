package com.linkee.linkeeapi.chat_room.query.controller;


import com.linkee.linkeeapi.chat.command.application.controller.query_controller.ChatRoomQueryController;
import com.linkee.linkeeapi.chat.command.application.service.query_serivce.ChatRoomQueryServiceImpl;
import com.linkee.linkeeapi.chat.command.domain.dto.query_dto.request.ChatRoomListRequestDto;
import com.linkee.linkeeapi.chat.command.domain.dto.query_dto.request.GameRoomListRequestDto;
import com.linkee.linkeeapi.chat.command.domain.dto.query_dto.response.ChatRoomListResponseDto;
import com.linkee.linkeeapi.chat.command.domain.dto.query_dto.response.GameRoomListResponseDto;
import com.linkee.linkeeapi.common.model.PageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ChatRoomQueryControllerTest {

    @Mock
    private ChatRoomQueryServiceImpl chatRoomQueryService;

    @InjectMocks
    private ChatRoomQueryController chatRoomQueryController;

    public ChatRoomQueryControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("✅ 자율방 목록 조회 성공 테스트")
    void getUserChatRooms_Success() {
        // given
        ChatRoomListRequestDto request = new ChatRoomListRequestDto();
        request.setUserId(1L);
        request.setPage(1);
        request.setSize(10);

        ChatRoomListResponseDto room1 = ChatRoomListResponseDto.builder()
                .chatRoomName("테스트 자율방 1")
                .ownerId(1L)
                .joinedCount(3)
                .build();

        ChatRoomListResponseDto room2 = ChatRoomListResponseDto.builder()
                .chatRoomName("테스트 자율방 2")
                .ownerId(2L)
                .joinedCount(4)
                .build();

        PageResponse<ChatRoomListResponseDto> pageResponse =
                new PageResponse<>(List.of(room1, room2), 1, 10, 2, 1);

        when(chatRoomQueryService.getMyChatRoomList(request)).thenReturn(pageResponse);

        // when
        ResponseEntity<PageResponse<ChatRoomListResponseDto>> response =
                chatRoomQueryController.getUserChatRooms(request);

        // then
        verify(chatRoomQueryService, times(1)).getMyChatRoomList(request);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).hasSize(2);
        assertThat(response.getBody().getContent().get(0).getChatRoomName()).isEqualTo("테스트 자율방 1");
    }

    @Test
    @DisplayName("✅ 게임방 목록 조회 성공 테스트")
    void getAllGameRooms_Success() {
        // given
        GameRoomListRequestDto request = new GameRoomListRequestDto();
        request.setPage(1);
        request.setSize(5);

        GameRoomListResponseDto game1 = GameRoomListResponseDto.builder()
                .chatRoomName("JAVA 퀴즈방")
                .isPrivate(com.linkee.linkeeapi.common.enums.Status.N)
                .ownerId(10L)
                .joinedCount(2)
                .roomCapacity(5)
                .build();

        PageResponse<GameRoomListResponseDto> pageResponse =
                new PageResponse<>(List.of(game1), 1, 5, 1, 1);

        when(chatRoomQueryService.getGameRoomList(request)).thenReturn(pageResponse);

        // when
        ResponseEntity<PageResponse<GameRoomListResponseDto>> response =
                chatRoomQueryController.getAllGameRooms(request);

        // then
        verify(chatRoomQueryService, times(1)).getGameRoomList(request);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent().get(0).getChatRoomName()).isEqualTo("JAVA 퀴즈방");
    }

}
