package com.linkee.linkeeapi.common.event;

import com.linkee.linkeeapi.question.command.domain.aggregate.Question;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class QuestionVerifiedEvent extends ApplicationEvent {
    private final Question question;

    public QuestionVerifiedEvent(Object source, Question question) {
        super(source);
        this.question = question;
    }
}
