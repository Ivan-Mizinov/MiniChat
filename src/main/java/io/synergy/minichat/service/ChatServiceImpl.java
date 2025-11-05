package io.synergy.minichat.service;

import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class ChatServiceImpl implements ChatService {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public String processMessage(String message) {
        if (message == null) {
            return "Не понял вопрос.";
        }

        String lowerMessage = message.trim().toLowerCase();

        if (lowerMessage.contains("который час") || lowerMessage.contains("время")) {
            LocalTime now = LocalTime.now();
            return "Сейчас " + now.format(formatter);
        }

        return "Я не знаю ответа на этот вопрос.";
    }
}
