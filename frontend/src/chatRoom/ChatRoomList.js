// chatRoom/ChatRoomList.js
import React from "react";

export default function ChatRoomList({ rooms, onSelect }) {
  return (
    <ul style={{ listStyle: "none", padding: 0 }}>
      {rooms.map(r => (
        <li
          key={r.id}
          onClick={() => onSelect(r)}
          style={{
            padding: "0.5rem",
            cursor: "pointer",
            borderBottom: "1px solid #eee",
          }}
        >
          {r.name}
        </li>
      ))}
    </ul>
  );
}
