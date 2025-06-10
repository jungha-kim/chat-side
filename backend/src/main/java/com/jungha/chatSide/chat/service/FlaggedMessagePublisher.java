package com.jungha.chatSide.chat.service;

import com.jungha.chatSide.chat.repository.FlaggedMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class FlaggedMessagePublisher {
    // 연결된 SSE 리스너 목록
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    // 클라이언트 연결 시 호출
    public SseEmitter register() {
        SseEmitter emitter = new SseEmitter(0L); // 타임아웃 없음
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout     (() -> emitters.remove(emitter));
        return emitter;
    }

    // 플래그 메시지 발생 시 호출
    public void publish(FlaggedMessage fm) {
        List<SseEmitter> dead = new CopyOnWriteArrayList<>();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("flagged")
                        .data(fm.getContent()));
            } catch (IOException e) {
                dead.add(emitter);
            }
        }
        emitters.removeAll(dead);
    }
}
