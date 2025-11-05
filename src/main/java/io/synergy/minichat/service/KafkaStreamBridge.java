package io.synergy.minichat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class KafkaStreamBridge {

    @Autowired
    private StreamBridge streamBridge;

    public void sendMessage(Message<String> message) {
        streamBridge.send("producer-out-0", message);
    }
}
