// src/setupProxy.js
const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
  // 1) SockJS 의 HTTP 폴링(info) + WS 업그레이드 모두 이 경로로 포워드
  app.use(
    '/ws',
    createProxyMiddleware({
      target: 'http://localhost:8080',
      changeOrigin: true,
      ws: true,    // ← WebSocket 업그레이드도 프록시
    })
  );
  // 2) REST /rooms API 도 포워드
  app.use(
    '/rooms',
    createProxyMiddleware({
      target: 'http://localhost:8080',
      changeOrigin: true,
    })
  );
};
