package com.jungha.chatSide.chat.controller;

import com.jungha.chatSide.chat.service.ChatRoomService;
import com.jungha.chatSide.chat.vo.ChatMessageVO;
import com.jungha.chatSide.chat.vo.ChatVO;
import org.apache.catalina.User;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class ChatRoomController {
    private final ChatRoomService roomService;
    private final SimpMessagingTemplate template;

    public ChatRoomController(ChatRoomService roomService, SimpMessagingTemplate template) {
        this.roomService = roomService;
        this.template = template;
    }


    // 2) 채팅 메시지 처리
    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload ChatMessageVO msg) {
        // 해당 방에만 발송
        template.convertAndSend("/topic/rooms/" + msg.getRoomId(), msg);
    }

    // 3) REST로 초기 방 목록 조회 (페이지 로드 시)
    @GetMapping("/rooms")
    public List<ChatVO> getMyRooms(@RequestHeader("client-id") String clientId) {
        return roomService.findRoomsByUser(clientId);
    }
    @PostMapping("/rooms/match")
    public ChatVO matchRoom(@RequestHeader("client-id") String clientId) {
        return roomService.matchRandomPartnerAndCreateRoom(clientId);
    }
}
