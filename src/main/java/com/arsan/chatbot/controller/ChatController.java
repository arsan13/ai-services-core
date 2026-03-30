package com.arsan.chatbot.controller;

import com.arsan.chatbot.dto.ChatResponse;
import com.arsan.chatbot.service.AiChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.ai.chat.memory.ChatMemory.DEFAULT_CONVERSATION_ID;

@RestController
@RequestMapping("/api/ai/chat")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class ChatController {

    private final AiChatService aiChatService;

    @GetMapping
    public ChatResponse chat(
            @RequestHeader(value = "userId", required = false, defaultValue = DEFAULT_CONVERSATION_ID) String userId,
            @RequestParam String message) {
        try {
            String response = aiChatService.generateResponse(message, userId);
            return new ChatResponse(response);
        } catch (Exception e) {
            log.error("Error generating response: {}", e.getMessage());
            return new ChatResponse("Sorry, something went wrong while processing your request. Try again later.");
        }
    }

    @GetMapping("/history")
    public List<Message> getChatHistory(
            @RequestHeader(value = "userId", required = false, defaultValue = DEFAULT_CONVERSATION_ID) String userId) {
        return aiChatService.getChatHistory(userId);
    }
}

