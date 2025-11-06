package com.linkee.linkeeapi.common.event;

import com.linkee.linkeeapi.alarm_box.command.application.dto.request.AlarmBoxCreateRequest;
import com.linkee.linkeeapi.alarm_box.command.application.service.AlarmBoxCommandService;
import com.linkee.linkeeapi.alarm_template.query.dto.response.AlarmTemplateResponse;
import com.linkee.linkeeapi.alarm_template.query.mapper.AlarmTemplateMapper;
import com.linkee.linkeeapi.common.sse.service.SseService;
import com.linkee.linkeeapi.inquiry.command.domain.aggregate.Inquiry;
import com.linkee.linkeeapi.question.command.domain.aggregate.Question;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

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
}
