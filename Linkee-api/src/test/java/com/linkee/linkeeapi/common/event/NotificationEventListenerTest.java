package com.linkee.linkeeapi.common.event;

import com.linkee.linkeeapi.alarm_box.command.application.dto.request.AlarmBoxCreateRequest;
import com.linkee.linkeeapi.alarm_box.command.application.service.AlarmBoxCommandService;
import com.linkee.linkeeapi.alarm_template.query.dto.response.AlarmTemplateResponse;
import com.linkee.linkeeapi.alarm_template.query.mapper.AlarmTemplateMapper;
import com.linkee.linkeeapi.comment.command.domain.aggregate.Comment;
import com.linkee.linkeeapi.common.sse.service.SseService;
import com.linkee.linkeeapi.inquiry.command.domain.aggregate.Inquiry;
import com.linkee.linkeeapi.question.command.domain.aggregate.Question;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationEventListenerTest {


    @InjectMocks
    private NotificationEventListener notificationEventListener;

    @Mock
    private AlarmBoxCommandService alarmBoxCommandService;

    @Mock
    private AlarmTemplateMapper alarmTemplateMapper;

    @Mock
    private SseService sseService;

    // 1. 친구 요청 알림 테스트
    @Test
    @DisplayName("친구 요청 이벤트가 발생했을 떄, 올바른 내용으로 알림 상자를 생성하고 SSE 알림을 전송한다.")
    void handleFriendRequestEvent_Should_CreateAndSendAlarm() {
        // given
        User requester = User.builder().userId(1L).userNickname("요청자").build();
        User receiver = User.builder().userId(2L).userNickname("수신자").build();
        FriendRequestEvent event = new FriendRequestEvent(this, requester, receiver);

        String templateContent = "님이 친구 요청을 보냈습니다.";
        AlarmTemplateResponse mockTemplate = new AlarmTemplateResponse(1L, templateContent, null, null);

        when(alarmTemplateMapper.selectAlarmTemplateById(1L)).thenReturn(mockTemplate);
        // when (실행)
        notificationEventListener.handleFriendRequestEvent(event);

        ArgumentCaptor<AlarmBoxCreateRequest> alarmRequestCaptor = ArgumentCaptor.forClass(AlarmBoxCreateRequest.class);
        verify(alarmBoxCommandService, times(1)).createAlarmBox(alarmRequestCaptor.capture());

        AlarmBoxCreateRequest capturedRequest = alarmRequestCaptor.getValue();
        assertThat(capturedRequest.getUserId()).isEqualTo(receiver.getUserId()); // 수신자 ID 검증
        assertThat(capturedRequest.getAlarmBoxContent()).isEqualTo("요청자님이 친구 요청을 보냈습니다."); // 알림 내용 검증

        verify(sseService, times(1)).send(
                receiver.getUserId(),
                "newNotification",
                "요청자님이 친구 요청을 보냈습니다."
        );
    }

    // 3. 문의
    @Test
    @DisplayName("문의 답변 이벤트 발생 시, 문의 작성자에게 알림을 전송한다")
    void handleInquiryAnsweredEvent_Should_SendAlarmToInquirer() {
        // given
        User inquirer = User.builder().userId(20L).userNickname("문의자").build();
        Inquiry inquiry = Inquiry.builder().inquiryId(1L).user(inquirer).build(); // 문의 객체에 문의자 연결
        InquiryAnsweredEvent event = new InquiryAnsweredEvent(this, inquiry);

        String templateContent = "문의하신 내용에 답변이 등록되었습니다.";
        AlarmTemplateResponse mockTemplate = new AlarmTemplateResponse(3L, templateContent, null, null);

        when(alarmTemplateMapper.selectAlarmTemplateById(3L)).thenReturn(mockTemplate);
        // when (실행)
        notificationEventListener.handleInquiryAnsweredEvent(event);

        ArgumentCaptor<AlarmBoxCreateRequest> alarmRequestCaptor = ArgumentCaptor.forClass(AlarmBoxCreateRequest.class);
        verify(alarmBoxCommandService, times(1)).createAlarmBox(alarmRequestCaptor.capture());

        AlarmBoxCreateRequest capturedRequest = alarmRequestCaptor.getValue();
        assertThat(capturedRequest.getUserId()).isEqualTo(inquirer.getUserId()); // 수신자 ID 검증
        assertThat(capturedRequest.getAlarmBoxContent()).isEqualTo(templateContent); // 알림 내용 검증

        verify(sseService, times(1)).send(
                inquirer.getUserId(),
                "inquiryAnswered",
                templateContent
        );


    }

    // 4. 문제 검증 알림 테스트
    @Test
    @DisplayName("문제 검증 완료 이벤트가 발생했을 때, 문제 작성자에게 알림을 전송한다.")
    void handleQuestionVerifiedEvent_Should_SendAlarmToOwner() {
        // given
        User questionOwner = User.builder().userId(10L).userNickname("문제작성자").build();
        Question verifiedQuestion = Question.builder().user(questionOwner).build();
        QuestionVerifiedEvent event = new QuestionVerifiedEvent(this, verifiedQuestion);

        String templateContent = "회원님의 문제가 검증되었습니다.";
        AlarmTemplateResponse mockTemplate = new AlarmTemplateResponse(4L, templateContent, null, null);

        when(alarmTemplateMapper.selectAlarmTemplateById(4L)).thenReturn(mockTemplate);
        // when (실행)
        notificationEventListener.handleQuestionVerifiedEvent(event);

        ArgumentCaptor<AlarmBoxCreateRequest> alarmRequestCaptor = ArgumentCaptor.forClass(AlarmBoxCreateRequest.class);
        verify(alarmBoxCommandService, times(1)).createAlarmBox(alarmRequestCaptor.capture());

        AlarmBoxCreateRequest capturedRequest = alarmRequestCaptor.getValue();
        assertThat(capturedRequest.getUserId()).isEqualTo(questionOwner.getUserId()); // 수신자 ID 검증
        assertThat(capturedRequest.getAlarmBoxContent()).isEqualTo(templateContent); // 알림 내용 검증

        verify(sseService, times(1)).send(
                questionOwner.getUserId(),
                "questionVerified",
                templateContent
        );
    }

    // 5. 댓글 알림
    @Test
    @DisplayName("원댓글 생성 시, 질문 작성자에게 알림을 전송한다")
    void handleCommentCreatedEvent_ForRootComment_Should_NotifyQuestionOwner() {
        // given (준비)
        User questionOwner = User.builder().userId(30L).userNickname("질문주인").build();
        User commenter = User.builder().userId(31L).userNickname("댓글쓴이").build();
        Question question = Question.builder().questionId(100L).user(questionOwner).build();
        Comment rootComment = Comment.builder()
                .commentId(1000L)
                .user(commenter)
                .question(question)
                .parent(null) // 원댓글이므로 부모가 없음
                .build();
        CommentCreatedEvent event = new CommentCreatedEvent(this, rootComment);

        String templateContent = "새로운 댓글이 달렸습니다.";
        AlarmTemplateResponse mockTemplate = new AlarmTemplateResponse(5L, templateContent, null, null);

        when(alarmTemplateMapper.selectAlarmTemplateById(5L)).thenReturn(mockTemplate);
        // when (실행)
        notificationEventListener.handleCommentCreatedEvent(event);

        ArgumentCaptor<AlarmBoxCreateRequest> alarmRequestCaptor = ArgumentCaptor.forClass(AlarmBoxCreateRequest.class);
        verify(alarmBoxCommandService, times(1)).createAlarmBox(alarmRequestCaptor.capture());

        AlarmBoxCreateRequest capturedRequest = alarmRequestCaptor.getValue();
        assertThat(capturedRequest.getUserId()).isEqualTo(questionOwner.getUserId()); // 수신자가 질문 주인인지 검증
        assertThat(capturedRequest.getAlarmBoxContent()).isEqualTo(templateContent);

        verify(sseService, times(1)).send(questionOwner.getUserId(), "newComment", templateContent);
    }

    // 6. 대댓글 알림
    @Test
    @DisplayName("대댓글 생성 시, 질문 작성자와 부모 댓글 작성자에게 알림을 전송한다")
    void handleCommentCreatedEvent_ForReply_Should_NotifyParentAndQuestionOwner() {
        // given
        User questionOwner = User.builder().userId(30L).userNickname("질문주인").build();
        User parentCommenter = User.builder().userId(31L).userNickname("원댓글쓴이").build();
        User replyCommenter = User.builder().userId(32L).userNickname("대댓글쓴이").build();

        Question question = Question.builder().questionId(100L).user(questionOwner).build();
        Comment parentComment = Comment.builder()
                .commentId(1000L)
                .user(parentCommenter)
                .question(question)
                .build();
        Comment replyComment = Comment.builder()
                .commentId(1001L)
                .user(replyCommenter)
                .question(question)
                .parent(parentComment)
                .build();
        CommentCreatedEvent event = new CommentCreatedEvent(this, replyComment);

        String templateContent = "회원님의 댓글에 새로운 답글이 달렸습니다.";
        AlarmTemplateResponse mockTemplate = new AlarmTemplateResponse(6L, templateContent, null, null);

        when(alarmTemplateMapper.selectAlarmTemplateById(6L)).thenReturn(mockTemplate);

        // when (실행)
        notificationEventListener.handleCommentCreatedEvent(event);

        // then (검증)
        ArgumentCaptor<AlarmBoxCreateRequest> alarmRequestCaptor = ArgumentCaptor.forClass(AlarmBoxCreateRequest.class);

        verify(alarmBoxCommandService, times(2)).createAlarmBox(alarmRequestCaptor.capture());

        List<Long> recipientIds = alarmRequestCaptor.getAllValues().stream()
                .map(AlarmBoxCreateRequest::getUserId)
                .toList();

        assertThat(recipientIds).containsExactlyInAnyOrder(
                questionOwner.getUserId(),
                parentCommenter.getUserId()
        );
    }
}
