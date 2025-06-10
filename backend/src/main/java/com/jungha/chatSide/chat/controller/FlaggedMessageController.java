package com.jungha.chatSide.chat.controller;

import com.jungha.chatSide.chat.service.FlaggedMessagePublisher;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
@CrossOrigin(origins="*")
@RestController
public class FlaggedMessageController {
    private final FlaggedMessagePublisher publisher;

    public FlaggedMessageController(FlaggedMessagePublisher publisher) {
        this.publisher = publisher;
    }

    @GetMapping(path = "/stream/flagged", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamFlagged() {
        return publisher.register();
    }
}
