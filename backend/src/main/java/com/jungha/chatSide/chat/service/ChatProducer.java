package com.jungha.chatSide.chat.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatProducer {
    private final KafkaTemplate<String,String> kafka;

    public ChatProducer(KafkaTemplate<String,String> kafka) {
        this.kafka = kafka;
    }

    public void send(String msg) {
        kafka.send("chat-messages", msg);
    }
}
