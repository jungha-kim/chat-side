package com.jungha.chatSide.chat.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class ClientIdHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest servletReq) {
            // 헤더에서 꺼내기
            String clientId = servletReq.getServletRequest().getHeader("client-id");
            // 또는 URL 파라미터로 보냈다면:
            if (clientId == null) {
                clientId = servletReq.getServletRequest().getParameter("client-id");
            }
            if (clientId != null) {
                attributes.put("client-id", clientId);
            }
        }
        return true;
    }
    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) { }
}
