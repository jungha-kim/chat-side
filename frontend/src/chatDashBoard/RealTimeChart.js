import { useEffect, useState } from "react";
import { LineChart, Line, XAxis, YAxis, Tooltip } from "recharts";

export default function RealTimeChart() {
  const [data, setData] = useState([]);

  useEffect(() => {
    const es = new EventSource("http://localhost:8080/stream/counts");
    es.addEventListener("count", e => {
      const [ts, cnt] = e.data.split(",");
      setData(d => [...d.slice(-59), { time: ts.slice(11,16), count: +cnt }]);
    });
    return () => es.close();
  }, []);

  return (
    <LineChart width={600} height={300} data={data}>
      <XAxis dataKey="time" />
      <YAxis />
      <Tooltip />
      <Line type="monotone" dataKey="count" />
    </LineChart>
  );
}