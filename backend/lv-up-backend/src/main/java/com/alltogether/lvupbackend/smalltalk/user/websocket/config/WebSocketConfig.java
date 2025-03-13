package com.alltogether.lvupbackend.smalltalk.user.websocket.config;

import com.alltogether.lvupbackend.login.util.JwtTokenProvider;
import com.alltogether.lvupbackend.smalltalk.user.websocket.interceptor.WebSocketHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.alltogether.lvupbackend.smalltalk.user.websocket.handler.WebSocketHandler;

// WebSocket 설정을 위한 클래스
@Configuration // 스프링의 설정 클래스임을 나타냄
@EnableWebSocket // WebSocket 기능 활성화
public class WebSocketConfig implements WebSocketConfigurer { // WebSocketConfigurer: WebSocket 설정 커스터마이징 가능하게 해주는
                                                              // 인터페이스, registerWebSocketHandlers 메서드 구현 필수

    private final WebSocketHandler webSocketHandler;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public WebSocketConfig(WebSocketHandler webSocketHandler, JwtTokenProvider jwtTokenProvider) {
        this.webSocketHandler = webSocketHandler;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // WebSocket 핸들러 등록
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) { // WebSocket 핸들러와 URL 경로 매핑, CORS 설정
        registry.addHandler(webSocketHandler, "/ws/match") // WebSocket 연결
                .addInterceptors(new WebSocketHandshakeInterceptor(jwtTokenProvider))
                .setAllowedOrigins("*"); // CORS 설정 - 모든 도메인 접근 허용 (특정 도메인만 허용하도록 수정하는 것이 좋음)
                
    }

    /*
     * // MatchWebSocketHandler를 빈으로 등록
     * 
     * @Bean // 스프링 컨테이너에 빈 등록
     * public WebSocketHandler matchWebSocketHandler() {
     * return new WebSocketHandler(); // 핸들러 인스턴스 생성 및 반환
     * }
     * 
     * 
     */

}
