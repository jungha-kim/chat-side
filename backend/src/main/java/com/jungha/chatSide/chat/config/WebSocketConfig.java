package com.jungha.chatSide.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final ClientIdHandshakeInterceptor handshakeInterceptor;
    private final PresenceChannelInterceptor  presenceInterceptor;


    public WebSocketConfig(ClientIdHandshakeInterceptor handshakeInterceptor,
                           PresenceChannelInterceptor presenceInterceptor) {
        this.handshakeInterceptor = handshakeInterceptor;
        this.presenceInterceptor  = presenceInterceptor;
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");        // 클라이언트 구독용 엔드포인트
        config.setApplicationDestinationPrefixes("/app");           // 클라이언트 발행용 prefix
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws")
                .addInterceptors(handshakeInterceptor)    // ← HandshakeInterceptor 등록
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(presenceInterceptor);
    }
}
