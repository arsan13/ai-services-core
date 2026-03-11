package com.arsan.chatbot.controller;

import com.arsan.chatbot.service.AiChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/chatbot/")
@RequiredArgsConstructor
public class ChatController {

    private final AiChatService aiChatService;

    @GetMapping("/chat")
    public String chat(@RequestParam String message) throws IOException {
        return aiChatService.generateResponse(message);
    }
}
