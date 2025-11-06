package com.linkee.linkeeapi.quiz.command.application.controller.command_controller;


import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import org.springframework.stereotype.Controller;


import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class TestSocketController {

    private final SimpMessageSendingOperations messaging;

    // 클라: stomp.send("/pub/test", {}, "hello")
    // 클라: stomp.subscribe("/sub/test", ...)
    @MessageMapping("/test")
    public void testEcho(Principal principal, @Payload String payload) {
        String who = (principal != null ? principal.getName() : "anon"); // null-safe
        String body = "[OK] who=" + who + ", payload=" + payload;
        messaging.convertAndSend("/sub/test", body);
        System.out.println("WS /pub/test -> " + body);
    }
}