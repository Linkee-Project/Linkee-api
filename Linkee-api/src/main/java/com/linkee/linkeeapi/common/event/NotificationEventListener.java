package com.linkee.linkeeapi.common.event;

import com.linkee.linkeeapi.alarm_box.command.application.dto.request.AlarmBoxCreateRequest;
import com.linkee.linkeeapi.alarm_box.command.application.service.AlarmBoxCommandService;
import com.linkee.linkeeapi.alarm_template.query.dto.response.AlarmTemplateResponse;
import com.linkee.linkeeapi.alarm_template.query.mapper.AlarmTemplateMapper;
import com.linkee.linkeeapi.comment.command.domain.aggregate.Comment;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.common.sse.service.SseService;
import com.linkee.linkeeapi.inquiry.command.domain.aggregate.Inquiry;
import com.linkee.linkeeapi.question.command.domain.aggregate.Question;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Slf4j

@Component
@RequiredArgsConstructor
public class NotificationEventListener {
    private final AlarmBoxCommandService alarmBoxCommandService;
    private final AlarmTemplateMapper alarmTemplateMapper;
    private final SseService sseService;

    // 1. 친구 요청 알림
    @EventListener
    public void handleFriendRequestEvent(FriendRequestEvent event) {
        User requester = event.getRequester();
        User receiver = event.getReceiver();

        AlarmTemplateResponse alarmTemplate = alarmTemplateMapper.selectAlarmTemplateById(1L);
        String alarmContent = String.format("%s%s", requester.getUserNickname(), alarmTemplate.templateContent());

        AlarmBoxCreateRequest alarmBoxCreateRequest = AlarmBoxCreateRequest.builder()
                .alarmBoxContent(alarmContent)
                .userId(receiver.getUserId())
                .build();
        alarmBoxCommandService.createAlarmBox(alarmBoxCreateRequest);

        sseService.send(receiver.getUserId(), "newNotification", alarmContent);

    }

    // 3. 문의 답변 등록 알림
    @EventListener
    public void handleInquiryAnsweredEvent(InquiryAnsweredEvent event) {
        Inquiry inquiry = event.getInquiry();
        User inquirer = inquiry.getUser(); // 문의를 작성한 사용자

        AlarmTemplateResponse alarmTemplate = alarmTemplateMapper.selectAlarmTemplateById(3L);
        String alarmContent = alarmTemplate.templateContent();

        AlarmBoxCreateRequest alarmBoxCreateRequest = AlarmBoxCreateRequest.builder()
                .alarmBoxContent(alarmContent)
                .userId(inquirer.getUserId())
                .build();
        alarmBoxCommandService.createAlarmBox(alarmBoxCreateRequest);

        sseService.send(inquirer.getUserId(), "inquiryAnswered", alarmContent);
    }

    // 4. 문제 검증 완료 알림
    @EventListener
    public void handleQuestionVerifiedEvent(QuestionVerifiedEvent event) {
        Question question = event.getQuestion();
        User questionOwner = question.getUser();

        AlarmTemplateResponse alarmTemplate = alarmTemplateMapper.selectAlarmTemplateById(4L);
        String alarmContent = alarmTemplate.templateContent();

        AlarmBoxCreateRequest alarmBoxCreateRequest = AlarmBoxCreateRequest.builder()
                .alarmBoxContent(alarmContent)
                .userId(questionOwner.getUserId())
                .build();
        alarmBoxCommandService.createAlarmBox(alarmBoxCreateRequest);

        sseService.send(questionOwner.getUserId(), "questionVerified", alarmContent);
    }

    // 5. 댓글 생성 알림
    @EventListener
    public void handleCommentCreatedEvent(CommentCreatedEvent event) {
        Comment savedComment = event.getComment();
        User commentOwner = savedComment.getUser();
        Question question = savedComment.getQuestion();

        long templateId;

        Set<Long> recipients = new HashSet<>();

        if (savedComment.getParent() == null) {
            templateId = 5L; // 댓글 알림

            Long questionOwnerId = question.getUser().getUserId();
            if (!questionOwnerId.equals(commentOwner.getUserId())) {
                recipients.add(questionOwnerId);
            }
        } else {
            templateId = 6L; // 대댓글 알림

            Comment parentComment = savedComment.getParent();
            Long parentCommentOwnerId = parentComment.getUser().getUserId();
            if (!parentCommentOwnerId.equals(commentOwner.getUserId())) {
                recipients.add(parentCommentOwnerId);
            }

            Long questionOwnerId = question.getUser().getUserId();
            if (!questionOwnerId.equals(commentOwner.getUserId()) && !questionOwnerId.equals(parentCommentOwnerId)) {
                recipients.add(questionOwnerId);
            }
        }

        String alarmContent;
        try {
            AlarmTemplateResponse templateResponse = alarmTemplateMapper.selectAlarmTemplateById(templateId);
            if (templateResponse == null || templateResponse.templateContent() == null) {
                log.error("code: {}, message: {} (templateId: {})",
                        ErrorCode.ALARM_TEMPLATE_NOT_FOUND.getCode(),
                        ErrorCode.ALARM_TEMPLATE_NOT_FOUND.getMessage(),
                        templateId);
                return;
            }
            alarmContent = templateResponse.templateContent();
        } catch (Exception e) {
            log.error("code: {}, message: {} (templateId: {})",
                    ErrorCode.ALARM_TEMPLATE_NOT_FOUND.getCode(),
                    "알림 템플릿 조회 중 데이터베이스 오류 발생",
                    templateId, e);
            return;
        }

        recipients.forEach(recipientId -> {
            AlarmBoxCreateRequest alarmRequest = new AlarmBoxCreateRequest(alarmContent, recipientId);
            alarmBoxCommandService.createAlarmBox(alarmRequest);
            sseService.send(recipientId, "newComment", alarmContent);
        });
    }

    // 6. 퀴즈방 초대 알림
    @EventListener
    public void handleQuizRoomInvite(QuizRoomInviteEvent event) {
        // 1. 이벤트에서 필요한 정보 추출
        Long inviteeId = event.getInviteeId();
        String inviterNickname = event.getInviterNickname();
        String roomTitle = event.getRoomTitle();
        // 2. 알림 템플릿 조회
        AlarmTemplateResponse alarmTemplate = alarmTemplateMapper.selectAlarmTemplateById(2L);
        // 3. 알림 내용 구성
        String alarmContent = String.format(alarmTemplate.templateContent(), inviterNickname, roomTitle);
        // 4. AlarmBox에 알림 저장(영속화)
        AlarmBoxCreateRequest alarmBoxCreateRequest = AlarmBoxCreateRequest.builder()
                .alarmBoxContent(alarmContent)
                .userId(inviteeId)
                .build();
        alarmBoxCommandService.createAlarmBox(alarmBoxCreateRequest);
        // 5. 실시간 SEE 알림 전송
        sseService.send(inviteeId, "quizRoomInvite", alarmContent);

    }
}
