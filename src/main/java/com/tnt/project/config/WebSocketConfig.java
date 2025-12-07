package com.tnt.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 구독 주소 prefix: /topic, /queue
        registry.enableSimpleBroker("/topic", "/queue");

        // 클라이언트에서 보낼 때 prefix: /app/...
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 리액트에서 연결할 엔드포인트 (SockJS 제거)
        registry.addEndpoint("/ws-stomp")
                // 개발용 허용 origin (필요한 IP만 넣기)
                .setAllowedOriginPatterns(
                        "http://localhost:3000",
                        "http://10.5.5.11:3000",
                        "http://10.5.5.12:3000",
                        "http://10.5.5.19:3000",
                        "http://10.5.5.20:3000",
                        "http://192.168.119.210:3000",
                        "http://192.168.219.108:3000"
                );
              
    }
}
