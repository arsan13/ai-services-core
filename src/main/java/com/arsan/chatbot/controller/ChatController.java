package com.arsan.chatbot.controller;

import com.arsan.chatbot.model.ChatResponse;
import com.arsan.chatbot.service.AiChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ai/chat")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class ChatController {

    private final AiChatService aiChatService;

    @GetMapping
    public ChatResponse chat(@RequestParam String message) throws Exception {
        try {
            String response = aiChatService.generateResponse(message);
            return new ChatResponse(response);
        } catch (Exception e) {
            log.error("Error generating response: {}", e.getMessage());
            return  new ChatResponse("Sorry, something went wrong while processing your request. Try again later.");
        }
    }

    @GetMapping("/history")
    public List<Message> getChatHistory() {
        return aiChatService.getChatHistory();
    }
}

