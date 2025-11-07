package com.linkee.linkeeapi.common.event;

import com.linkee.linkeeapi.inquiry.command.domain.aggregate.Inquiry;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class InquiryAnsweredEvent extends ApplicationEvent {

    private final Inquiry inquiry;

    public InquiryAnsweredEvent(Object source, Inquiry inquiry) {
        super(source);
        this.inquiry = inquiry;
    }
}
