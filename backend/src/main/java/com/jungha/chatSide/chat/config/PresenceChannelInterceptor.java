package com.jungha.chatSide.chat.config;

import com.jungha.chatSide.chat.repository.ChatRoomRepository;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class PresenceChannelInterceptor implements ChannelInterceptor {
    private final ChatRoomRepository repo;

    public PresenceChannelInterceptor(ChatRoomRepository repo) {
        this.repo = repo;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String simpUser = accessor.getUser() != null
                ? accessor.getUser().getName()
                : null;

        if (StompCommand.CONNECT.equals(accessor.getCommand()) && simpUser != null) {
            repo.registerActiveUser(simpUser);
        }
        else if (StompCommand.DISCONNECT.equals(accessor.getCommand()) && simpUser != null) {
            repo.unregisterActiveUser(simpUser);
        }
        return message;
    }


}
