package com.linkee.linkeeapi.common.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class QuizRoomInviteEvent extends ApplicationEvent {

    private final Long inviteeId;
    private final String roomTitle;
    private final String inviterNickname;

    public QuizRoomInviteEvent(Object source, Long inviteeId, String inviterNickname, String roomTitle) {
        super(source);
        this.inviteeId = inviteeId;
        this.inviterNickname = inviterNickname;
        this.roomTitle = roomTitle;
    }
}
