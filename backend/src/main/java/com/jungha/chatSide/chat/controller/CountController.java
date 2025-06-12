package com.jungha.chatSide.chat.controller;

import com.jungha.chatSide.chat.service.CountPublisher;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@CrossOrigin(origins="*")
public class CountController {
    private final CountPublisher publisher;

    public CountController(CountPublisher publisher) {
        this.publisher = publisher;
    }

    @GetMapping("/stream/counts")
    public SseEmitter streamCounts() {
        return publisher.register();
    }
}
