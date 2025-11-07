package io.synergy.minichat.controller;

import io.synergy.minichat.service.ChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/api/service/processMessage")
    public String processMessage(@RequestParam("message") String message) {
        return chatService.processMessage(message);
    }
}
