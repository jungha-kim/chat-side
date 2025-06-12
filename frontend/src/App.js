// frontend/src/App.js
import React from "react";
import RealTimeFlagged from "./chatDashBoard/RealTimeFlagged";
import RealTimeChart  from "./chatDashBoard/RealTimeChart";

function App() {
  return (
    <div className="App">
      <RealTimeFlagged />
      <RealTimeChart />
    </div>
  );
}

export default App;