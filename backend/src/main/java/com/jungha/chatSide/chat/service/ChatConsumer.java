package com.jungha.chatSide.chat.service;

import com.jungha.chatSide.chat.repository.FlaggedMessage;
import com.jungha.chatSide.chat.repository.FlaggedMessageRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChatConsumer {
    private final ModerationService modSvc;
    private final FlaggedMessageRepository repo;
    private final KafkaTemplate<String,String> kafka;

    private final FlaggedMessagePublisher publisher;

    public ChatConsumer(ModerationService modSvc, FlaggedMessageRepository repo, KafkaTemplate<String,String> kafka, FlaggedMessagePublisher publisher) {
        this.modSvc = modSvc;
        this.repo = repo;
        this.kafka = kafka;
        this.publisher = publisher;
    }

    @KafkaListener(topics = "chat-messages", groupId = "chat-side-group")
    public void listen(String msg) {
        if (modSvc.isFlagged(msg)) {
            //DB 저장
            FlaggedMessage fm = new FlaggedMessage();
            fm.setContent(msg);
            fm.setReason("금칙어 포함");
            fm.setTimestamp(LocalDateTime.now());
            repo.save(fm);

            //별도 토픽으로도 발행
            kafka.send("flagged-messages", msg);
            //SSE 클라이언트에 실시한 push
            publisher.publish(fm);
        } else {
            System.out.println("[IN] " + msg);
        }
    }
}
