package com.jungha.chatSide.chat.controller;

import com.jungha.chatSide.chat.service.ChatProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins="*")
public class ChatController {
    private final ChatProducer producer;

    public ChatController(ChatProducer producer) {
        this.producer = producer;
    }

    @PostMapping("/produce")
    public ResponseEntity<Void> produce(@RequestBody String message) {
        producer.send(message);
        return ResponseEntity.ok().build();
    }
}