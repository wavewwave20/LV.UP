package com.alltogether.lvupbackend.smalltalk.user.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import com.alltogether.lvupbackend.smalltalk.user.entity.Matching;
import com.alltogether.lvupbackend.smalltalk.user.repository.SmallTalkUserRepository;

@SpringBootTest
public class MatchServiceTest {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SmallTalkUserRepository smallTalkUserRepository;

    @Autowired
    private MatchService matchService;

    // Redis 데이터 초기화 및 테스트용 데이터 설정
    @BeforeEach
    void setUp() {
        // Redis 데이터 초기화
        redisTemplate.delete(redisTemplate.keys("*"));

        // 테스트를 위한 초기 데이터 설정
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        zSetOps.add("matchQueue", "user2", 100.0);
        zSetOps.add("matchQueue", "user3", 90.0);
        zSetOps.add("matchQueue", "user4", 80.0);
    }

    @AfterEach
    void tearDown() {
        // 테스트 종료 후 Redis 데이터 정리
        redisTemplate.delete(redisTemplate.keys("*"));
    }

    // docker exec -it redis-server redis-cli
    // AUTH password
    @Test
    void testRequestMatch_Success() throws Exception {
        // 테스트 데이터 설정
        String userId = "user1";
        double score = 100.0;

        // 현재 Redis 상태 확인
        Set<String> initialUsers = redisTemplate.opsForZSet().range("matchQueue", 0, -1);
        System.out.println("초기 대기열 상태: " + initialUsers);

        // 매칭 요청 실행
        //String result = matchService.requestMatch(userId, score);
        //System.out.println(result);
        String result = "OK";

        // 결과 확인
        // assertTrue(result.startsWith("✅ 매칭 완료: ["));
        // assertTrue(result.contains("user2"));
        // assertTrue(result.contains("user3"));
        // assertTrue(result.contains("user4"));

        // Redis에서 매칭된 사용자들이 제거되었는지 확인
        Set<String> remainingUsers = redisTemplate.opsForZSet().range("matchQueue", 0, -1);
        System.out.println("남은 대기열 상태: " + remainingUsers);

        // 매칭 결과 확인
        if (result.contains("대기")) {
            System.out.println("대기 상태 확인");
            Double userScore = redisTemplate.opsForZSet().score("matchQueue", userId);
            System.out.println("사용자 점수: " + userScore);
            assertTrue(userScore != null);
        } else {
            System.out.println("매칭 완료 상태 확인");
            assertTrue(result.startsWith("✅ 매칭 완료: ["));
            assertTrue(remainingUsers == null || remainingUsers.isEmpty());

            // 세션 정보 확인
            String sessionId = result.split("세션 ID: ")[1].replace("]", "");
            System.out.println("세션 ID: " + sessionId);

            HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
            String matchSessionKey = "matchSession:" + sessionId;
            Map<String, String> sessionInfo = hashOps.entries(matchSessionKey);
            System.out.println("세션 존재 여부: " + !sessionInfo.isEmpty());
            assertTrue(sessionInfo.isEmpty());
        }
    }

    @Test
    void testEndSession() {
        // 테스트 데이터 설정
        String sessionId = "test_session";
        LocalDateTime startTime = LocalDateTime.now().minusMinutes(5);
        List<String> userIds = Arrays.asList("user1", "user2", "user3");

        // 세션 종료 실행
        //matchService.endSession(sessionId, userIds, startTime);

        // Redis에서 세션이 제거되었는지 확인
        assertFalse(redisTemplate.hasKey(sessionId));

        // DB에 기록이 저장되었는지 확인
        verify(smallTalkUserRepository).save(any(Matching.class));
    }
}

/*
 * Mock을 이용하는 것이라 실제로 redis 통신을 하지 않음
 *
 * import com.alltogether.lvupbackend.smalltalk.user.entity.MatchHistory;
 * import com.alltogether.lvupbackend.smalltalk.user.repository.
 * SmallTalkUserRepository;
 * import com.alltogether.lvupbackend.smalltalk.user.websocket.handler.
 * MatchWebSocketHandler;
 *
 * import org.junit.jupiter.api.BeforeEach;
 * import org.junit.jupiter.api.Test;
 * import org.mockito.InjectMocks;
 * import org.mockito.Mock;
 * import org.mockito.MockitoAnnotations;
 * import org.springframework.data.redis.core.StringRedisTemplate;
 * import org.springframework.data.redis.core.ZSetOperations;
 * import org.springframework.data.redis.core.RedisTemplate;
 * import org.springframework.data.redis.core.HashOperations;
 *
 * import java.time.LocalDateTime;
 * import java.util.Arrays;
 * import java.util.HashSet;
 * import java.util.Set;
 * import java.util.concurrent.TimeUnit;
 *
 * import static org.junit.jupiter.api.Assertions.assertEquals;
 * import static org.junit.jupiter.api.Assertions.assertTrue;
 * import static org.mockito.ArgumentMatchers.*;
 * import static org.mockito.Mockito.*;
 *
 *
 * public class MatchServiceTest {
 *
 * @Mock
 * private StringRedisTemplate redisTemplate;
 *
 * @Mock
 * private SmallTalkUserRepository smallTalkUserRepository;
 *
 * @Mock
 * private MatchWebSocketHandler webSocketHandler;
 *
 * @Mock
 * private ZSetOperations<String, String> zSetOps;
 *
 * @InjectMocks
 * private MatchService matchService;
 *
 * @BeforeEach
 * void setUp() {
 * MockitoAnnotations.openMocks(this);
 * when(redisTemplate.opsForZSet()).thenReturn(zSetOps);
 * }
 *
 * @Test
 * void testRequestMatch_Success() throws Exception {
 * // 테스트 데이터 설정
 * String userId = "user1";
 * double score = 100.0;
 * Set<String> candidates = new HashSet<>(Arrays.asList("user2", "user3",
 * "user4"));
 *
 * // zSetOps.rangeByScore가 candidates를 반환 하는지
 * // - 점수 범위 내의 사용자 목록을 반환 하는지지
 * when(zSetOps.rangeByScore(anyString(), anyDouble(), anyDouble()))
 * .thenReturn(candidates);
 *
 * // zSetOps.remove가 호출되는지
 * //doNothing().when(zSetOps).remove(anyString(), anyString());
 *
 * // redisTemplate.opsForHash().put가 호출되는지
 * // hash 동작 확인
 *
 * @SuppressWarnings("unchecked") // 제너릭 관련 타입 안정성 경고 무시시
 * HashOperations<String, Object, Object> hashOps = mock(HashOperations.class);
 * when(redisTemplate.opsForHash()).thenReturn(hashOps);
 * //when(hashOps.put(anyString(), anyString(), anyString())).thenReturn(null);
 * //put 메서드가 void 반화 donothing 필요요
 * doNothing().when(hashOps).put(anyString(), anyString(), anyString());
 *
 * // redisTemplate.expire가 호출되는지
 * // 세션 만료 동작 확인
 * //doNothing().when(redisTemplate).expire(anyString(), anyLong(), any());
 * when(redisTemplate.expire(anyString(), anyLong(),
 * any(TimeUnit.class))).thenReturn(true);
 *
 * // webSocketHandler.notifyMatch가 호출되는지
 * // webSocket을 통한 알림 설정
 * doNothing().when(webSocketHandler).notifyMatch(anyString(), anyString());
 *
 * // requestMatch 메소드가 정상적으로 동작하는지
 * // 매칭 요청 실행
 * String result = matchService.requestMatch(userId, score);
 *
 * // 결과 확인
 * //assertEquals("✅ 매칭 완료: [user2, user3, user4] [세션 ID: " +
 * result.split(": ")[1] + "]", result);
 * assertTrue(result.startsWith("✅ 매칭 완료: ["));
 * assertTrue(result.contains("user2"));
 * assertTrue(result.contains("user3"));
 * assertTrue(result.contains("user4"));
 *
 * // 검증
 * // verify(zSetOps, times(3)).remove(anyString(), anyString());
 * // verify(redisTemplate, times(1)).opsForHash().put(anyString(), anyString(),
 * anyString());
 * // verify(redisTemplate, times(1)).expire(anyString(), anyLong(), any());
 * // verify(webSocketHandler, times(3)).notifyMatch(anyString(), anyString());
 *
 * // ZSet 작업 검증
 * verify(zSetOps).rangeByScore(anyString(), anyDouble(), anyDouble()); // 매칭 가능
 * 사용자 조회회
 * verify(zSetOps, times(3)).remove(anyString(), anyString()); // userId 포함하여 4명
 *
 * // Hash 작업 검증
 * verify(hashOps).put(anyString(), anyString(), anyString()); // 세션 정보 저장
 *
 * // Expire 작업 검증
 * verify(redisTemplate).expire(anyString(), anyLong(), any()); // 세션 만료 시간 설정정
 *
 * // Websocket 작업 검증
 * verify(webSocketHandler, times(3)).notifyMatch(anyString(), anyString()); //
 * 모두에게 알림림
 * }
 *
 * @Test
 * void testEndSession() {
 * String sessionId = "session1";
 * LocalDateTime startTime = LocalDateTime.now().minusMinutes(5);
 * LocalDateTime endTime = LocalDateTime.now();
 * String userIdsJson = "user1,user2,user3";
 * MatchHistory matchHistory = MatchHistory.builder()
 * .matchingModeId(1)
 * .state("ACT")
 * .interestId(1)
 * .dialogue(userIdsJson)
 * .createdAt(startTime)
 * .endedAt(endTime)
 * .build();
 *
 * when(smallTalkUserRepository.save(any(MatchHistory.class))).thenReturn(
 * matchHistory);
 * when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));
 * doNothing().when(webSocketHandler).notifyMatch(anyString(), anyString());
 *
 * matchService.endSession(sessionId, Arrays.asList("user1", "user2", "user3"),
 * startTime);
 *
 * verify(smallTalkUserRepository).save(any(MatchHistory.class));
 * verify(redisTemplate.opsForHash()).delete(eq(sessionId), anyString());
 * verify(webSocketHandler, times(3)).notifyMatch(anyString(), anyString());
 * }
 * }
 */