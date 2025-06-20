// App.js
import React, { useState, useEffect } from "react";
import ChatRoomList from "./chatRoom/ChatRoomList";
import ChatRoom     from "./chatRoom/ChatRoom";

export default function App({ currentUser }) {
  const [rooms, setRooms] = useState([]);
  const [activeRoom, setActiveRoom] = useState(null);

  // 1) 내 방 목록 로드
  useEffect(() => {
	  fetch("/rooms", {
	    headers: { "client-id": currentUser }
	  })                 // 내 계정에 속한 방만 반환
      .then(r => r.json())
      .then(setRooms)
      .catch(console.error);
  }, []);

  // 2) 매칭해서 새 방 만들기
  const handleMatch = () => {
	  fetch("/rooms/match", {
	    method: "POST",
	    headers: { "client-id": currentUser }
	  })
      .then(r => r.json())           // { id, name, participants: [...] }
      .then(newRoom => {
        setRooms(prev => [...prev, newRoom]);
        setActiveRoom(newRoom);
      })
      .catch(console.error);
  };

  return (
    <div style={{ display: "flex", height: "100vh" }}>
      <aside style={{ width: 250, borderRight: "1px solid #ddd", padding: "1rem" }}>
        <button onClick={handleMatch} style={{ marginBottom: "1rem", width: "100%" }}>
          새로운 대화하기
        </button>
        <ChatRoomList
          rooms={rooms}
          onSelect={r => setActiveRoom(r)}
        />
      </aside>

      <main style={{ flex: 1, padding: "1rem" }}>
        {activeRoom
          ? <ChatRoom room={activeRoom} currentUser={currentUser}/>
          : <p>왼쪽에서 방을 선택하거나, 새로운 대화를 시작하세요.</p>
        }
      </main>
    </div>
  );
}
