package com.jungha.chatSide.chat.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ModerationService {
    /*
    * 욕설 리스트
    */
    private static final List<String> BANNED = Arrays.asList("욕설1", "스팸키워드");

    /*욕설 FLAG*/
    public boolean isFlagged(String msg) {
        String lower = msg.toLowerCase();
        return BANNED.stream().anyMatch(lower::contains);
    }
}
