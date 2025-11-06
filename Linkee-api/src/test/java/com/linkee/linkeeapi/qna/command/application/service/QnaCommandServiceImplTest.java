package com.linkee.linkeeapi.qna.command.application.service;

import com.linkee.linkeeapi.chat_member.command.domain.aggregate.entity.ChatMember;
import com.linkee.linkeeapi.chat_member.command.infrastructure.repository.ChatMemberRepository;
import com.linkee.linkeeapi.chat_room.command.domain.aggregate.ChatRoom;
import com.linkee.linkeeapi.chat_room.command.infrastructure.repository.JpaChatRoomRepository;
import com.linkee.linkeeapi.qna.command.application.dto.request.CreateQnaRequestDto;
import com.linkee.linkeeapi.qna.command.domain.aggregate.Qna;
import com.linkee.linkeeapi.qna.command.infrastructure.repository.JpaQnaRepository;
import com.linkee.linkeeapi.user.command.application.service.util.UserFinder;
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
    private JpaChatRoomRepository chatRoomRepository;

    @Mock
    private ChatMemberRepository chatMemberRepository;

    @Mock
    private JpaQnaRepository qnaRepository;

    @InjectMocks
    private QnaCommandServiceImpl qnaCommandService;

    private ChatRoom chatRoom;
    private ChatMember chatMember;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        chatRoom = ChatRoom.builder()
                .chatRoomId(1L)
                .build();

        chatMember = ChatMember.builder()
                .chatMemberId(1L)
                .build();
    }

    @Test
    @DisplayName("Qna 생성 성공")
    void testCreateQna_Success() {
        CreateQnaRequestDto request = CreateQnaRequestDto.builder()
                .question("문제 내용")
                .answer("정답")
                .roomId(chatRoom.getChatRoomId())
                .chatMemberId(chatMember.getChatMemberId())
                .build();

        when(chatMemberRepository.findById(chatMember.getChatMemberId()))
                .thenReturn(Optional.of(chatMember));
        when(chatRoomRepository.findById(chatRoom.getChatRoomId()))
                .thenReturn(Optional.of(chatRoom));

        qnaCommandService.createQna(request);

        verify(qnaRepository, times(1)).save(any(Qna.class));
    }

    @Test
    @DisplayName("Qna 생성 실패 - 존재하지 않는 채팅방 멤버")
    void testCreateQna_Fail_ChatMemberNotFound() {
        CreateQnaRequestDto request = CreateQnaRequestDto.builder()
                .question("문제 내용")
                .answer("정답")
                .roomId(chatRoom.getChatRoomId())
                .chatMemberId(chatMember.getChatMemberId())
                .build();

        when(chatMemberRepository.findById(chatMember.getChatMemberId()))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> qnaCommandService.createQna(request));

        assertEquals("존재하지 않는 채팅방 member 입니다", exception.getMessage());
        verify(qnaRepository, never()).save(any(Qna.class));
    }

    @Test
    @DisplayName("Qna 생성 실패 - 존재하지 않는 채팅방")
    void testCreateQna_Fail_ChatRoomNotFound() {
        CreateQnaRequestDto request = CreateQnaRequestDto.builder()
                .question("문제 내용")
                .answer("정답")
                .roomId(chatRoom.getChatRoomId())
                .chatMemberId(chatMember.getChatMemberId())
                .build();

        when(chatMemberRepository.findById(chatMember.getChatMemberId()))
                .thenReturn(Optional.of(chatMember));
        when(chatRoomRepository.findById(chatRoom.getChatRoomId()))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> qnaCommandService.createQna(request));

        assertEquals("존재하지 않는 채팅방입니다.", exception.getMessage());
        verify(qnaRepository, never()).save(any(Qna.class));
    }


}
