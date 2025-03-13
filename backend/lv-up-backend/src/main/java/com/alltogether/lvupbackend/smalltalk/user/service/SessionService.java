package com.alltogether.lvupbackend.smalltalk.user.service;

import com.alltogether.lvupbackend.security.prop.MatchSessionProperties;
import com.alltogether.lvupbackend.smalltalk.user.dto.SessionInfoDTO;
import com.alltogether.lvupbackend.smalltalk.user.websocket.handler.WebSocketHandler;
import com.alltogether.lvupbackend.smalltalk.user.websocket.event.WebSocketMessageEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionService {

    private final RedisTemplate<String, String> redisTemplate;
<<<<<<< HEAD

    private MatchSessionProperties matchSessionProperties;
=======
    private final MatchSessionProperties matchSessionProperties;
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher eventPublisher;

    // 각 세션의 준비 상태를 저장할 Map
    private final Map<String, Set<Integer>> sessionReadyUsers = new ConcurrentHashMap<>();
>>>>>>> develop

    /**
     * 유저 ID로 유효한 세션인지 확인
     *
     * @param sessionId 세션 ID
     * @param userId 유저 ID
     * @return 세션 유효 여부
     */
    public boolean isUserValidSession(Integer sessionId, Integer userId) {
        Map<Object, Object> sessions = redisTemplate.opsForHash().entries(matchSessionProperties.getSession());

        // 세션에 해당하는 유저를 찾음
        String matchedUsers = (String) sessions.get(sessionId.toString());

        // 해당 세션이 없는 경우
        if (matchedUsers == null) {
            return false;
        }

        // 세션에 포함된 유저 목록에서 userId가 있는지 확인
        return Arrays.asList(matchedUsers.split(",")).contains(userId.toString());
    }

    /**
     * 세션에 포함된 다른 유저 찾기
     *
     * @param sessionId 세션 ID
     * @param userId 유저 ID
     * @return 세션에 포함된 다른 유저 ID
     */
    public String findOtherUserInSession(Integer sessionId, Integer userId) {
        Map<Object, Object> sessions = redisTemplate.opsForHash().entries(matchSessionProperties.getSession());

        // 세션에 해당하는 유저를 찾음
        String matchedUsers = (String) sessions.get(sessionId.toString());

        // 해당 세션이 없는 경우
        if (matchedUsers == null) {
            return null;
        }

        // 세션에 포함된 유저 목록에서 userId가 있는지 확인
        String[] users = matchedUsers.split(",");
        for (String user : users) {
            if (!user.equals(userId.toString())) {
                return user;
            }
        }

        return null;
    }
<<<<<<< HEAD
=======

    /**
     * 세션에서 해당 유저아이디를 가진 매칭 아이디 반환
     * 
     * @param userId 유저 아이디
     * @return 매칭 아이디
     */
    public String findMatchingIdByUserId(Integer userId) {
        Map<Object, Object> sessions = redisTemplate.opsForHash().entries(matchSessionProperties.getSession());

        for (Map.Entry<Object, Object> entry : sessions.entrySet()) {
            String sessionUsers = (String) entry.getValue();
            List<String> userList = Arrays.stream(sessionUsers.split(":")[1]
                    .trim()
                    .replaceAll("[\\[\\]]", "")
                    .split(","))
                    .map(String::trim)
                    .toList();

            if (userList.contains(userId.toString())) {
                return sessionUsers.split(":")[0].trim();
            }
        }

        return null;
    }

    /**
     * 스트림 준비 상태 처리
     * 
     * @param userId    유저 ID
     * @param sessionId 세션 ID
     */
    public void handleStreamReady(Integer userId, String sessionId) {
        try {
            // 해당 세션의 준비된 유저 목록에 추가
            sessionReadyUsers.computeIfAbsent(sessionId, k -> ConcurrentHashMap.newKeySet()).add(userId);

            // 세션의 모든 유저가 준비되었는지 확인
            Map.Entry<String, List<String>> sessionInfo = getSessionByUserId(userId);
            if (sessionInfo != null) {
                Set<Integer> readyUsers = sessionReadyUsers.get(sessionId);
                List<String> sessionUsers = sessionInfo.getValue();

                log.info("세션 준비 상태 체크 - 세션: {}, 준비된 유저: {}/{}",
                        sessionId, readyUsers.size(), sessionUsers.size());

                if (readyUsers.size() == sessionUsers.size()) {
                    // 이벤트를 통해 메시지 전송
                    Map<String, Object> messageMap = new HashMap<>();
                    messageMap.put("type", "CONNECTION_READY");
                    messageMap.put("serverTime", System.currentTimeMillis());

                    String message = objectMapper.writeValueAsString(messageMap);
                    for (String user : sessionUsers) {
                        // WebSocket 이벤트 발행
                        eventPublisher.publishEvent(new WebSocketMessageEvent(
                                Integer.parseInt(user.trim()),
                                message));
                    }

                    sessionReadyUsers.remove(sessionId);
                    log.info("모든 유저 준비 완료 - 세션: {}", sessionId);
                }
            }
        } catch (Exception e) {
            log.error("스트림 준비 상태 처리 중 오류 발생", e);
        }
    }

    /**
     * 세션 준비 상태 초기화
     * 
     * @param sessionId 세션 ID
     */
    public void clearSessionReady(String sessionId) {
        sessionReadyUsers.remove(sessionId);
        log.info("세션 준비 상태 초기화 완료 - 세션: {}", sessionId);
    }

    /**
     * 특정 유저가 포함된 세션정보 파싱해서 가져오기
     *
     * @param userId 유저아이디
     * @return SessionInfoDTO 객체
     */
    public SessionInfoDTO findSessionInfoByUserId(Integer userId) {
        log.info("{} 가 포함된 세션 정보 요청 됨", userId);
        Map<Object, Object> sessions = redisTemplate.opsForHash().entries(matchSessionProperties.getSession());

        for (Map.Entry<Object, Object> entry : sessions.entrySet()) {
            String sessionUsers = (String) entry.getValue();

            // "matchingId:[user1, user2]" 형태로 저장된 값 처리
            String[] sessionParts = sessionUsers.split(":");

            if (sessionParts.length < 2) {
                log.error("세션 데이터 형식이 올바르지 않습니다. sessionUsers: {}", sessionUsers);
                continue;
            }

            String matchingId = sessionParts[0].trim();
            List<Integer> userList = Arrays.stream(sessionParts[1]
                    .trim()
                    .replaceAll("[\\[\\]]", "")
                    .split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .toList();

            if (userList.contains(userId)) {
                log.info("유저 아이디로 세션 정보 조회 결과가 존재합니다. userId: {}, sessionId: {}", userId, entry.getKey());
                return SessionInfoDTO.builder()
                        .sessionId((String) entry.getKey())
                        .matchingId(matchingId)
                        .userList(userList)
                        .build();
            }
        }

        log.error("유저 아이디로 세션 정보 조회 결과가 존재하지 않습니다. userId: {}", userId);
        return null;
    }

    /**
     * 해당 세션 아이디의 세션을 지움
     *
     * @param sessionId
     * @return 종료됨
     */
    public void deleteSession(String sessionId) {
        log.info("세션 종료중...{}", sessionId);

        redisTemplate.opsForHash().delete(matchSessionProperties.getSession(), sessionId);

        log.info("세션 종료 완료...{}", sessionId);
    }
>>>>>>> develop
}
