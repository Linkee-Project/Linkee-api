package com.linkee.linkeeapi.qna.command.application.service;

import com.linkee.linkeeapi.chat.chat_command.chat_domain.entity.ChatMember;
import com.linkee.linkeeapi.chat.chat_command.chat_repository.ChatMemberRepository;
import com.linkee.linkeeapi.chat.chat_command.chat_domain.entity.ChatRoom;
import com.linkee.linkeeapi.chat.chat_command.chat_repository.ChatRoomRepository;
import com.linkee.linkeeapi.qna.command.application.dto.request.CreateQnaRequestDto;
import com.linkee.linkeeapi.qna.command.domain.aggregate.Qna;
import com.linkee.linkeeapi.qna.command.infrastructure.repository.JpaQnaRepository;
import com.linkee.linkeeapi.user.command.application.service.util.UserFinder;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QnaCommandServiceImplTest {

    @Mock
    private UserFinder userFinder;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ChatMemberRepository chatMemberRepository;

    @Mock
    private JpaQnaRepository qnaRepository;

    @InjectMocks
    private QnaCommandServiceImpl qnaCommandService;

    private ChatRoom chatRoom;
    private ChatMember chatMember;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder().userId(1L).build();

        chatRoom = ChatRoom.builder()
                .chatRoomId(1L)
                .build();

        chatMember = ChatMember.builder()
                .chatMemberId(1L)
                .user(user)
                .chatRoom(chatRoom)
                .build();
    }

    @Test
    @DisplayName("Qna 생성 성공")
    void testCreateQna_Success() {
        CreateQnaRequestDto request = CreateQnaRequestDto.builder()
                .question("문제 내용")
                .answer("정답")
                .roomId(chatRoom.getChatRoomId())
                .build();

        when(chatRoomRepository.findById(chatRoom.getChatRoomId()))
                .thenReturn(Optional.of(chatRoom));
        when(chatMemberRepository.findByChatRoomAndUser_UserId(chatRoom, user.getUserId()))
                .thenReturn(Optional.of(chatMember));

        qnaCommandService.createQna(request, user.getUserId());

        verify(qnaRepository, times(1)).save(any(Qna.class));
    }

    @Test
    @DisplayName("Qna 생성 실패 - 존재하지 않는 채팅방")
    void testCreateQna_Fail_ChatRoomNotFound() {
        CreateQnaRequestDto request = CreateQnaRequestDto.builder()
                .question("문제 내용")
                .answer("정답")
                .roomId(chatRoom.getChatRoomId())
                .build();

        when(chatRoomRepository.findById(chatRoom.getChatRoomId()))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> qnaCommandService.createQna(request, user.getUserId()));

        assertEquals("존재하지 않는 채팅방입니다.", exception.getMessage());
        verify(qnaRepository, never()).save(any(Qna.class));
    }

    @Test
    @DisplayName("Qna 생성 실패 - 채팅방에 속하지 않은 멤버")
    void testCreateQna_Fail_ChatMemberNotFound() {
        CreateQnaRequestDto request = CreateQnaRequestDto.builder()
                .question("문제 내용")
                .answer("정답")
                .roomId(chatRoom.getChatRoomId())
                .build();

        when(chatRoomRepository.findById(chatRoom.getChatRoomId()))
                .thenReturn(Optional.of(chatRoom));
        when(chatMemberRepository.findByChatRoomAndUser_UserId(chatRoom, user.getUserId()))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> qnaCommandService.createQna(request, user.getUserId()));

        assertEquals("채팅방에 속한 멤버가 아닙니다.", exception.getMessage());
        verify(qnaRepository, never()).save(any(Qna.class));
    }
}