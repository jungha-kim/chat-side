package com.jungha.chatSide.chat.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class CountStreamListener {
    private final CountPublisher publisher;

    public CountStreamListener(CountPublisher publisher) {
        this.publisher = publisher;
    }

    @KafkaListener(topics = "flagged-counts", groupId = "chat-side-gorup")
    public void listen(ConsumerRecord<String, String> record) {
        // record 형식: "2025-06-10T17:23:00,5"
        long epochMs = Long.parseLong(record.key());
        LocalDateTime ldt = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(epochMs),
                ZoneId.of("Asia/Seoul")
        );
        String count       = record.value(); // "5"

        // 이제 split 필요 없이 바로 사용
        System.out.printf("윈도우 %s 에서 count=%s%n", ldt, count);
        publisher.publish(ldt.toString(), count);
    }
}
