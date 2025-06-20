package com.jungha.chatSide.chat.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jungha.chatSide.chat.repository.ChatRoomRepository;
import com.jungha.chatSide.chat.vo.ChatVO;

@Service
public class ChatRoomService {
    private final ChatRoomRepository repository;
    // userId -> roomId list
    private final Map<String, List<String>> userRooms = new ConcurrentHashMap<>();

    @Autowired
    public ChatRoomService(ChatRoomRepository repository) {
        this.repository = repository;
    }

    /**
     * 사용자가 속한 모든 채팅방 조회
     */
    public List<ChatVO> findRoomsByUser(String userId) {
        List<String> roomIds = userRooms.getOrDefault(userId, List.of());
        return roomIds.stream()
                .map(repository::findById)
                .filter(vo -> vo != null)
                .collect(Collectors.toList());
    }

    /**
     * 새로운 랜덤 매칭 후 방 생성 및 양쪽에 방 목록 등록
     */
    public ChatVO matchRandomPartnerAndCreateRoom(String userId) {
        // repository에서 현재 접속 중인 사용자 목록 가져오기
        List<String> active = repository.findActiveUsers();
        // 본인을 제외한 랜덤 선택
        String partner = active.stream()
                .filter(id -> !id.equals(userId))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("No partner available"));

        // 방 생성
        ChatVO room = repository.createRoom(List.of(userId, partner));
        // 사용자별 방 목록에 추가
        userRooms.compute(userId, (k,v) -> v == null ? List.of(room.getId()) : append(v, room.getId()));
        userRooms.compute(partner, (k,v) -> v == null ? List.of(room.getId()) : append(v, room.getId()));
        return room;
    }

    /**
     * 새 방 생성만 (예: 이름으로)
     */
    public ChatVO create(String name) {
        return repository.createRoom(name);
    }

    // 간단히 리스트에 추가하는 헬퍼
    private List<String> append(List<String> list, String id) {
        return List.copyOf(
                Stream.concat(list.stream(), Stream.of(id))
                        .collect(Collectors.toList())
        );
    }
}
