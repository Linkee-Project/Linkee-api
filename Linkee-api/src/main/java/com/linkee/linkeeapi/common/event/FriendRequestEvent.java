package com.linkee.linkeeapi.common.event;

import com.linkee.linkeeapi.user.command.domain.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class FriendRequestEvent extends ApplicationEvent {
    private final User requester;
    private final User receiver;

    public FriendRequestEvent(Object source, User requester, User receiver) {
        super(source);
        this.requester = requester;
        this.receiver = receiver;
    }
}
