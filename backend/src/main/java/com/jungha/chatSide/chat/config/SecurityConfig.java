package com.jungha.chatSide.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1) CSRF 비활성화 (POST 요청을 허용하려면 꼭 필요)
                .csrf(csrf -> csrf.disable())

                // 2) REST, WebSocket 경로별로 permitAll 설정
                .authorizeHttpRequests(auth -> auth
                        // WebSocket 핸드셰이크 및 STOMP 메시징
                        .requestMatchers("/ws/**", "/app/**", "/topic/**").permitAll()

                        // 방 조회(GET), 매칭(POST) API
                        .requestMatchers(HttpMethod.GET,  "/rooms"       ).permitAll()
                        .requestMatchers(HttpMethod.POST, "/rooms/match" ).permitAll()

                        // 이 외 모든 요청은 인증 불가(403) 또는 필요 시 authenticated()
                        .anyRequest().denyAll()
                )

                // 3) HTTP Basic / Form 로그인 자체를 완전히 비활성화
                .httpBasic(basic -> basic.disable())
                .formLogin(login -> login.disable());

        return http.build();
    }
}
