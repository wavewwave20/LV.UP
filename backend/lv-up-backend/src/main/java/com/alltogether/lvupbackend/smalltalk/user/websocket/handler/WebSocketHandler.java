package com.alltogether.lvupbackend.smalltalk.user.websocket.handler;

import com.alltogether.lvupbackend.security.prop.MatchSessionProperties;
import com.alltogether.lvupbackend.smalltalk.user.service.SessionService;
import com.alltogether.lvupbackend.smalltalk.user.websocket.event.WebSocketDisconnectEvent;
import com.alltogether.lvupbackend.smalltalk.user.websocket.event.WebSocketMessageEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

// WebSocket 연결 처리 핸들러
// TextWebSocketHandler:
//      - 텍스트 기반 WebSocket 메시지 처리하는 기본 핸들러 클래스
//      - 바이너리 메시지가 아닌 텍스트만 처리
@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {
    // 연결된 모든 WebSocket 세션을 저장하는 Map
    // ConcurrentHashmap을 사용하여 동시성 문제 처리
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Map<Integer, String> userSessionMap = new ConcurrentHashMap<>();

    private final ApplicationEventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class); // Logger 선언 추가

    private final RedisTemplate<Object, Object> redisTemplate;
    private final MatchSessionProperties matchSessionProperties;
    private final SessionService sessionService;

    // WebSocket 연결이 성공적일 때 호출 됨
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        try {
            String userId = session.getAttributes().get("userId").toString();
            log.info("Websocket 연결 요청 들어옴. userId: {}", userId);

            // 기존 세션이 있는지 확인
            String oldSessionId = userSessionMap.get(Integer.parseInt(userId));
            if (oldSessionId != null) {
                WebSocketSession oldSession = sessions.get(oldSessionId);
                if (oldSession != null && oldSession.isOpen()) {
                    log.info("기존 세션 종료: {}", oldSessionId);
                    oldSession.close(new CloseStatus(1000, "중복 세션 종료"));
                }
                sessions.remove(oldSessionId);
            }

            // 새 세션 등록
            sessions.put(session.getId(), session);
            userSessionMap.put(Integer.parseInt(userId), session.getId());

            log.info("Websocket 연결됨: {} for user: {}", session.getId(), userId);
        } catch (Exception e) {
            log.error("연결 설정 중 오류 발생: ", e);
        }
    }

    // WebSocket 연결 삭제
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
<<<<<<< HEAD
        logger.info("연결 종료 상태: {}, 사유: {}", status.getCode(), status.getReason());
=======
        log.warn("웹소켓 연결 종료 발생: {}, 사유: {}", status.getCode(), status);
>>>>>>> develop

        String sessionId = session.getId();

        Integer userId = userSessionMap.entrySet().stream()
                .filter(entry -> sessionId.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        log.warn("Websocket 연결 종료됨: {}, userId: {}", sessionId, userId);

        if (userId != null) {
            log.info("Websocket 연결이 종료된 유저: " + userId);

            sessions.remove(sessionId); // 세션 삭제
            userSessionMap.remove(userId); // 사용자 세션 매핑 삭제

            log.info("매칭 유저 대기열 확인 후 삭제, 매칭 세션 삭제 확인 후 삭제");
            eventPublisher.publishEvent(new WebSocketDisconnectEvent(userId));
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        System.err.println("WebSocket error: " + exception.getMessage());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 메시지 받음
        log.info("WebSocket 메시지 받음: {}", message.getPayload());
        try {
            JsonNode jsonMessage = objectMapper.readTree(message.getPayload());
            log.info("파싱된 메시지: {}", jsonMessage);

<<<<<<< HEAD
        // 클라이언트에서 ping 메시지 받음
        if ("ping".equals(message.getPayload())) {
            try {
                if (session.isOpen()) { // 세션이 열려 있는지 확인
                    session.sendMessage(new TextMessage("pong"));
                } else {
                    System.err.println("WebSocket 세션이 닫혀 있어 메시지를 보낼 수 없습니다.");
=======
            if (jsonMessage.has("type")) {
                String type = jsonMessage.get("type").asText();
                log.info("메시지 타입: {}", type);

                if ("STREAM_READY".equals(type)) {
                    Integer userId = getUserIdFromSession(session);
                    String sessionId = userSessionMap.get(userId);

                    // 해당 유저의 현제 openvidu 세션 찾기
                    Map.Entry<String, List<String>> openviduSession = sessionService.getSessionByUserId(userId);
                    String openviduSessionId = openviduSession.getKey();

                    if (sessionId != null) {
                        // Redis에 상태 저장
                        String readyKey = matchSessionProperties.getStream() + ":" + openviduSessionId;
                        redisTemplate.opsForHash().put(readyKey, userId.toString(), "ready");
                        redisTemplate.expire(readyKey, 5, TimeUnit.MINUTES);

                        Long readyCount = redisTemplate.opsForHash().size(readyKey);
                        log.info("스트림 준비 상태 체크 - 세션: {}, 준비된 유저: {}", openviduSessionId, readyCount);

                        // 양쪽 모두 준비되면 타이머 시작 메시지 전송
                        if (readyCount == 2) {
                            Map<String, Object> messageMap = new HashMap<>();
                            messageMap.put("type", "CONNECTION_READY");
                            messageMap.put("serverTime", System.currentTimeMillis());

                            String readyMessage = objectMapper.writeValueAsString(messageMap);
                            Set<Object> readyUsers = redisTemplate.opsForHash().keys(readyKey);
                            for (Object readyUserId : readyUsers) {
                                notifyMatch(Integer.parseInt(readyUserId.toString()), readyMessage);
                            }

                            redisTemplate.delete(readyKey);
                            log.info("모든 유저 준비 완료 - 세션: {}", openviduSession);
                        }
                    }
                } else if ("ping".equals(type)) {
                    log.info("ping 메시지 받음, pong 메시지 전송");
                    session.sendMessage(new TextMessage("{\"type\":\"pong\"}"));
>>>>>>> develop
                }
            }
        } catch (Exception e) {
            log.error("WebSocket 메시지 처리 중 오류 발생", e);
        }
    }

    // 웹소켓 연결 확인
    public boolean isConnected(String userId) {
        return userSessionMap.containsKey(userId);
    }

    // 매칭 알림을 특정 사용자에게 전송
    public void notifyMatch(Integer userId, String message) {
        log.info("notifyMatch() called for userId: {}, message: {}", userId, message);

        String sessionId = userSessionMap.get(userId);
        if (sessionId == null) {
            log.error("notifyMatch: userSessionMap에 해당 userId(" + userId + ")가 존재하지 않습니다.");
            return;
        }

        WebSocketSession session = sessions.get(sessionId);
        if (session == null) {
            log.error("notifyMatch: sessions에 해당 sessionId(" + sessionId + ")가 존재하지 않습니다.");
            return;
        }

        // 세션이 존재하고 열려있는 상태인 경우에만 메시지 전송
        if (session != null) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message)); // TextMessage객체로 메시지 전송
                    log.info("Message sent to user " + userId + ": " + message);
                } catch (IOException e) {
                    e.printStackTrace(); // 메시지 전송 실패시 스택 트레이스 출력
                }
            } else {
                log.error("WebSocket session for user " + userId + " is not open.");
            }
        }
    }

    // 매칭 요청 취소
    public void cancelRequestMatch(Integer userId) {
        log.info("매칭 요청 취소, 웹소켓 연결 해제. userId {}", userId);
        String user = userSessionMap.get(userId);
        if (user != null) {
            WebSocketSession session = sessions.get(user);
            if (session != null) {
                try {
                    session.close(new CloseStatus(1000, "매칭 취소로 인한 웹소켓 연결 해제"));
                    sessions.remove(user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // openvidu 세션 종료
    public void notifySessionClosed(Integer userId, String message) {
        // OPENVIDU 세션 종료 후 유저 연결 삭제
        log.info("notifySessionClosed() 호출: " + userId);
        String userSessionId = userSessionMap.get(userId);

        if (userSessionId != null) {
            log.info("유저 웹소켓 세션: " + userSessionId);
            WebSocketSession session = sessions.get(userSessionId);

            if (session != null && session.isOpen()) {
                log.info("유저 " + userId + "가 " + session + "에 연결되어 있음");
                try {
                    log.info("✅ WebSocket 연결 종료: " + userSessionId + ", " + session);

                    session.sendMessage(new TextMessage(message));
                    session.close(new CloseStatus(1000, "종료됨"));

                    sessions.remove(userSessionId);
                    userSessionMap.remove(userId);

                } catch (IOException e) {
                    log.error("❌ WebSocket 연결 종료 실패: " + e.getMessage());
                }
            }
        }
    }

    // 세션에서 userId 가져오기
    private Integer getUserIdFromSession(WebSocketSession session) {
        return Integer.parseInt(session.getAttributes().get("userId").toString());
    }

    // userSessionMap에서 sessionId 가져오기
    private String getSessionIdFromUserId(Integer userId) {
        return userSessionMap.get(userId);
    }

    @EventListener
    public void handleWebSocketMessageEvent(WebSocketMessageEvent event) {
        notifyMatch(event.getUserId(), event.getMessage());
    }

    /**
     * WebSocketHandler 필요시 메서드 추가
     * 
     * handleTextMessage: 클라이언트로 부터 메시지 받았을 떄 처리
     * afterConnectionClosed: WebSocket 연결이 종료 되었을 떄 처리
     * handlerTransportError: 전송 중 오류가 발생했을 때 처리
     * 
     */
}
