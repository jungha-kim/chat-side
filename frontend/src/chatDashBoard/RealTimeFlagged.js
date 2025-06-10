// frontend/src/RealTimeFlagged.js
import { useEffect, useState } from "react";

export default function RealTimeFlagged() {
  //상태 훅: 받은 메시지 배열
  const [messages, setMessages] = useState([]);

  useEffect(() => {
    //SSE 연결: 백엔드 엔드포인트 지정 (프록시 설정 없을 때)
    const evtSource = new EventSource('http://localhost:8080/stream/flagged');

    //"flagged" 이벤트가 오면 배열 앞쪽에 추가
    evtSource.addEventListener("flagged", e => {
      setMessages(prev => [e.data, ...prev]);
    });

    //에러 시 로그 찍고 연결 닫기
    evtSource.onerror = () => {
      console.error("SSE 연결 오류");
      evtSource.close();
    };

    //컴포넌트 언마운트 시 연결 해제
    return () => {
      evtSource.close();
    };
  }, []); // 빈 배열: 마운트(최초 렌더) 시 한 번만 실행

  return (
    <div style={{ padding: "1rem" }}>
      <h2>🚩 실시간 플래그된 메시지</h2>
      <ul>
        {messages.map((msg, i) => (
          <li key={i}>{msg}</li>
        ))}
      </ul>
    </div>
  );
}