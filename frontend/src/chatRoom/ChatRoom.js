import { useEffect, useState, useRef } from "react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { getClientId } from '../utils/ClientId';

export default function ChatRoom({ room, currentUser }) {
  // 방별 메시지 히스토리를 관리
  const [msgsMap, setMsgsMap] = useState({});
  const [text, setText] = useState("");
  const [connected, setConnected] = useState(false);

  const clientRef = useRef(null);
  const subRef = useRef(null);

  // STOMP 클라이언트 초기화 및 연결 (한 번만)
  useEffect(() => {
    const clientId = getClientId();
    const stomp = new Client({
      connectHeaders: { "client-id": clientId },
      webSocketFactory: () => new SockJS("/ws"),
      reconnectDelay: 5000,
      onConnect: () => setConnected(true),
      onWebSocketError: err => console.error(err),
      onStompError: frame => console.error(frame),
    });
    stomp.activate();
    clientRef.current = stomp;

    return () => {
      if (subRef.current) {
        subRef.current.unsubscribe();
        subRef.current = null;
      }
      stomp.deactivate();
    };
  }, [/* … */]);

  // room.id 변경 시 구독 전환 및 메시지 저장
  useEffect(() => {
    const client = clientRef.current;
    if (!client) return;

    // 이전 구독 해제
    if (subRef.current) {
      subRef.current.unsubscribe();
      subRef.current = null;
    }

    // 해당 room 메시지 배열 초기화
    setMsgsMap(prev => prev[room.id] ? prev : { ...prev, [room.id]: [] });

    // 구독 함수
    const subscribeToRoom = () => {
      subRef.current = client.subscribe(
        `/topic/rooms/${room.id}`,
        frame => {
          const body = JSON.parse(frame.body);
          setMsgsMap(prev => ({
            ...prev,
            [room.id]: [...(prev[room.id] || []), body],
          }));
        }
      );
    };

    if (client.connected) {
      subscribeToRoom();
    } else {
      const prev = client.onConnect;
      client.onConnect = () => {
        prev && prev();
        subscribeToRoom();
      };
    }
  }, [room.id]);

  const send = () => {
    if (!text || !connected) return;
    clientRef.current.publish({
      destination: "/app/sendMessage",
      body: JSON.stringify({
        roomId: room.id,
        sender: currentUser,
        content: text,
      }),
    });
    setText("");
  };

  // 현재 방 메시지
  const msgs = msgsMap[room.id] || [];

  return (
    <div style={{ border: '1px solid #ddd', padding: '1rem', borderRadius: '8px' }}>
      <h3>채팅방: {room.name}</h3>
      <div style={{ height: 300, overflowY: "auto", padding: '0.5rem', background: '#f9f9f9' }}>
        {msgs.map((m, i) => {
          const isMine = m.sender === currentUser;
          return (
            <div
              key={i}
              style={{
                display: 'flex',
                justifyContent: isMine ? 'flex-end' : 'flex-start',
                margin: '0.25rem 0',
              }}
            >
              <div
                style={{
                  maxWidth: '60%',
                  padding: '0.5rem 1rem',
                  borderRadius: '16px',
                  background: isMine ? '#acf' : '#eee',
                }}
              >
                <div style={{ fontSize: '0.8rem', marginBottom: '0.25rem', color: '#555' }}>
                  {isMine ? '나' : m.sender}
                </div>
                <div>{m.content}</div>
              </div>
            </div>
          );
        })}
      </div>
      <div style={{ display: 'flex', marginTop: '0.5rem' }}>
        <input
          value={text}
          onChange={e => setText(e.target.value)}
          onKeyDown={e => e.key === "Enter" && send()}
          placeholder="메시지를 입력하고 Enter"
          style={{ flex: 1, padding: '0.5rem', borderRadius: '16px', border: '1px solid #ccc' }}
        />
        <button onClick={send} disabled={!connected || !text} style={{ marginLeft: '0.5rem', padding: '0 1rem', borderRadius: '16px' }}>
          전송
        </button>
      </div>
    </div>
  );
}
