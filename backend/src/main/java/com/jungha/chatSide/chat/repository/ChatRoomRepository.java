package com.jungha.chatSide.chat.repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.jungha.chatSide.chat.vo.ChatVO;

@Repository
public class ChatRoomRepository {

    // 방 ID → 방 정보
    private final Map<String, ChatVO> rooms = new ConcurrentHashMap<>();
    // 방 ID → 참여자 목록
    private final Map<String, List<String>> participants = new ConcurrentHashMap<>();
    // 현재 접속(활성) 중인 사용자 ID 집합
    private final Set<String> activeUsers = ConcurrentHashMap.newKeySet();

    /**
     * 새 방 생성: 이름 기반
     */
    public ChatVO createRoom(String name) {
        String id = UUID.randomUUID().toString();
        ChatVO room = new ChatVO(id, name);
        rooms.put(id, room);
        // 이름 기반 생성은 참여자 정보가 없으므로 빈 리스트
        participants.put(id, new ArrayList<>());
        return room;
    }

    /**
     * 새 방 생성: 사용자 ID 리스트 기반 (매칭용)
     */
    public ChatVO createRoom(List<String> userIds) {
        String id = UUID.randomUUID().toString();
        // 채팅방 이름은 두 사용자의 ID를 합쳐서 간단히 표현
        String name = String.join(" vs ", userIds);
        ChatVO room = new ChatVO(id, name);
        rooms.put(id, room);
        participants.put(id, new ArrayList<>(userIds));
        return room;
    }

    /**
     * 방 정보 조회
     */
    public ChatVO findById(String roomId) {
        return rooms.get(roomId);
    }

    /**
     * 현재 접속 중인 사용자 ID 목록
     */
    public List<String> findActiveUsers() {
        return new ArrayList<>(activeUsers);
    }

    /**
     * WebSocket Connect 시 호출: 사용자를 “활성” 목록에 추가
     */
    public void registerActiveUser(String userId) {
        activeUsers.add(userId);
    }

    /**
     * WebSocket Disconnect 시 호출: 사용자를 “활성” 목록에서 제거
     */
    public void unregisterActiveUser(String userId) {
        activeUsers.remove(userId);
    }
}
