package com.linkee.linkeeapi.chat.chat_command.chat_controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatPageController {

    @GetMapping("/page")
    public String chatPage() {
        // templates/chat.html 렌더링
        return "chat";
    }
}
