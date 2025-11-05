package io.synergy.minichat.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
public class KafkaMessageService {

    @Autowired
    private ChatService chatService;

    @Autowired
    private KafkaStreamBridge kafkaStreamBridge;

    Deque<String> deque = new ArrayDeque<>();

    @PostConstruct
    public void init() {
        deque.add("Message 1");
        deque.add("Message 2");
        deque.add("Message 3");
        deque.add("Message 4");
        deque.add("Message 5");
    }

    @Bean
    public Supplier<Message<String>> producer() {
        return () -> {
            if (!deque.isEmpty()) {
                return MessageBuilder.withPayload(deque.poll()).build();
            } else {
                return null;
            }
        };
    }

    @Bean
    public Consumer<Message<String>> consumer() {
        return message -> {
            String question = message.getPayload();
            if (question == null) {
                return;
            }
            System.out.println("Получено сообщение: " + question);

            String answer = chatService.processMessage(question);
            Message<String> reply = MessageBuilder
                    .withPayload(answer)
                    .build();

            kafkaStreamBridge.sendMessage(reply);
        };
    }
}
