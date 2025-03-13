package com.alltogether.lvupbackend.smalltalk.user.service;

<<<<<<< HEAD
=======
import com.alltogether.lvupbackend.admin.repository.BlockRepository;
import com.alltogether.lvupbackend.asset.service.AssetHistoryService;
>>>>>>> develop
import com.alltogether.lvupbackend.common.dto.PageResponseDto;
import com.alltogether.lvupbackend.security.prop.MatchSessionProperties;
import com.alltogether.lvupbackend.smalltalk.user.dto.MatchHistoryDto;
import com.alltogether.lvupbackend.smalltalk.user.dto.MatchHistoryResponseDto;
import com.alltogether.lvupbackend.smalltalk.user.dto.MatchRequestDto;
import com.alltogether.lvupbackend.smalltalk.user.dto.MatchingModeResponseDto;
import com.alltogether.lvupbackend.smalltalk.user.entity.Matching;
import com.alltogether.lvupbackend.smalltalk.user.entity.MatchingMode;
import com.alltogether.lvupbackend.smalltalk.user.entity.MatchingUser;
import com.alltogether.lvupbackend.smalltalk.user.repository.MatchingModeRepository;
import com.alltogether.lvupbackend.smalltalk.user.repository.MatchingRepository;
import com.alltogether.lvupbackend.smalltalk.user.repository.MatchingUserRepository;
import com.alltogether.lvupbackend.smalltalk.user.repository.SmallTalkUserRepository;
import com.alltogether.lvupbackend.smalltalk.user.websocket.event.WebSocketDisconnectEvent;
import com.alltogether.lvupbackend.smalltalk.user.websocket.handler.WebSocketHandler;
import com.alltogether.lvupbackend.user.domain.User;
import com.alltogether.lvupbackend.user.repository.InterestRepository;
import com.alltogether.lvupbackend.user.repository.UserInterestRepository;
import com.alltogether.lvupbackend.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchService {
    private final StringRedisTemplate redisTemplate;
    private final SmallTalkUserRepository smallTalkUserRepository;
    //private final WebSocketHandler webSocketHandler;
    private final OpenViduService openViduService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

    private final MatchSessionProperties matchSessionProperties;

    private static final int GROUP_SIZE = 1;
    private static final int MATCH_DURATION = 5 * 60; // 5분
    private static final long TIME_THRESHOLD = 20 * 1000; // 20초

    private final MatchingModeRepository matchingModeRepository;
    private final ObjectMapper objectMapper;
    private final MatchingRepository matchingRepository;
    private final UserRepository userRepository;
    private final UserInterestRepository userInterestRepository;
    private final MatchingUserRepository matchingUserRepository;
    private final WebSocketHandler webSocketHandler;

<<<<<<< HEAD
=======
    private final MissionService missionService;
    private final SessionService sessionService;
    private final InterestRepository interestRepository;
    private final TopicInterestRepository topicInterestRepository;
    private final AssetHistoryService assetHistoryService;
    private final BlockRepository blockRepository;

>>>>>>> develop
    private String MATCH_QUEUE_KEY;
    private String MATCH_SESSION_KEY;
    private String MATCH_EXTEND_KEY;

    @PostConstruct
    private void init() {
        this.MATCH_QUEUE_KEY = matchSessionProperties.getQueue();
        this.MATCH_SESSION_KEY = matchSessionProperties.getSession();
        this.MATCH_EXTEND_KEY = matchSessionProperties.getExtend();
    }

    public ResponseEntity<?> addToMatchingQueue(MatchRequestDto request) {
        try {
<<<<<<< HEAD
=======
            if (sessionService.isUserInSession(request.getUserId())) {
                log.info("이미 매칭을 진행 중인 유저 입니다. userId: {}", request.getUserId());
                // return ResponseEntity.badRequest().body("이미 매칭을 진행 중인 유저 입니다.");
            }

>>>>>>> develop
            // 매칭 대기열에 존재하는지 확인
            List<String> queues = redisTemplate.opsForList().range(MATCH_QUEUE_KEY, 0, -1);
            for (String queue : queues) {
                try {
                    MatchRequestDto rq = objectMapper.readValue(queue, MatchRequestDto.class);
                    if (rq.getUserId().equals(request.getUserId())) {
                        log.info("대기열에서 매칭 요청을 찾음. userId: {}", request.getUserId());
                        return ResponseEntity.ok(Map.of("message", "이미 대기열에 존재하는 유저입니다."));
                    }
                } catch (JsonProcessingException e) {
                    log.error("대기열 검사 중 JSON 처리 오류 발생", e);
                }
            }

            // DB에서 유저 정보 존재 확인
            if (!userRepository.existsById(request.getUserId())) {
                log.error("유저 아이디가 존재하지 않습니다! userId: {}", request.getUserId());
                return ResponseEntity.ok("유저 아이디가 존재하지 않습니다!");
            }

            // 유저 관심사 찾기
            List<String> userInterests = userInterestRepository.findByUserUserId(request.getUserId())
                    .stream()
                    .map(userInterest -> String.valueOf(userInterest.getInterest().getInterestId()))
                    .toList();
            if(userInterests.isEmpty()) {
                log.info("해당 유저의 관심사는 존재하지 않습니다. userId {}", request.getUserId());
            } else {
                log.info("요청 유저의 관심사 정보. userId {}, 관심사: {}", request.getUserId(), userInterests);
                request.setInterests(userInterests);
            }

            // 유저 아바타 조회
            User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. userId: " + request.getUserId()));
            
            Integer userAvatarId = user.getAvatar() != null ? user.getAvatar().getAvatarId() : 0;
            request.setUserAvatarId(userAvatarId);

<<<<<<< HEAD
=======
            // 성별 추가
            request.setGender(user.getGender());
>>>>>>> develop

            // 대기열에 추가
            String jsonRequest = objectMapper.writeValueAsString(request);
            redisTemplate.opsForList().rightPush(MATCH_QUEUE_KEY, jsonRequest);
            log.info("매칭 대기열에 추가: {}", jsonRequest);

            // 대기열 추가후 바로 매칭 프로세스 실행
            // processMatchQueue();

            return ResponseEntity.ok("대기열에 추가되었습니다.");
        } catch (JsonProcessingException e) {
            log.error("매칭 요청 추가 중 오류 발생", e);
            return ResponseEntity.ok("매칭 요청 추가 중 JSON 오류 발생");
        } catch (RuntimeException e) {
<<<<<<< HEAD
            log.error("사용자를 찾을 수 없습니다", e);
            return ResponseEntity.ok("사용자를 찾을 수 없습니다");
=======
            log.error("데이터 조회 중 오류 발생", e);
            return ResponseEntity.badRequest().body("데이터 조회 중 오류 발생");
>>>>>>> develop
        } catch (Exception e) {
            return ResponseEntity.ok("매칭 요청 추가 중 오류 발생");
        }
    }

    // 7초마다 대기열 속 매칭 요청 처리
    @Scheduled(fixedDelay = 7000)
    public void processMatchQueue() {
        log.info("\uD83D\uDD0D 매칭 탐색 중...");

        // 대기열 속 모든 매칭 가져오기
        List<String> queues = redisTemplate.opsForList().range(MATCH_QUEUE_KEY, 0, -1);
        if (queues == null || queues.size() < 2) {
            log.info("대기열이 비어있습니다.");
            return;
        }

        Set<String> matchedRequests = new HashSet<>();

        for (String jsonRequest1 : queues) {
            try {
                if (matchedRequests.contains(jsonRequest1)) {
                    continue;
                }
                MatchRequestDto request1 = objectMapper.readValue(jsonRequest1, MatchRequestDto.class);
                boolean isRequest1TimeExpired = System.currentTimeMillis() - request1.getTimestamp() > TIME_THRESHOLD;

                for (String jsonRequest2 : queues) {
                    // 중복 유저 스킵
                    if (jsonRequest1.equals(jsonRequest2) || matchedRequests.contains(jsonRequest2)) {
                        continue;
                    }

                    MatchRequestDto request2 = objectMapper.readValue(jsonRequest2, MatchRequestDto.class);
<<<<<<< HEAD
                    boolean isRequest2TimeExpired = System.currentTimeMillis() - request2.getTimestamp() > TIME_THRESHOLD;
=======

                    // TODO: 블락유저 매칭 안 되게 할 시 활성화
                    // 각 유저가 블락 처리된 상태인지 확인 먼저
                    // List<Integer> blockList = blockRepository.findByUserId(request1.getUserId());
                    // blockList.addAll(blockRepository.findByUserId(request2.getUserId()));
                    //
                    // if (blockList.contains(request2.getUserId())) {
                    // log.info("블락 처리된 유저 입니다. {} - {}", request1.getUserId(),
                    // request2.getUserId());
                    // continue;
                    // }

                    boolean isRequest2TimeExpired = System.currentTimeMillis()
                            - request2.getTimestamp() > TIME_THRESHOLD;
>>>>>>> develop

                    // 매칭 조건 확인
                    boolean isMatched = false;
                    List<String> matchedInterests = null;

                    // 둘 다 1분 지난 경우
                    if (isRequest1TimeExpired && isRequest2TimeExpired) {
                        // 조건1 매칭 검사
                        if (matchCondition1(request1, request2)) {
                            matchedInterests = findCommonInterest(request1.getInterests(), request2.getInterests());
                            isMatched = true;
                        } else if (matchCondition2(request1, request2)) {
                            isMatched = true;
                        }
                    } else {
                        // 그 이외의 경우
                        if (matchCondition1(request1, request2)) {
                            matchedInterests = findCommonInterest(request1.getInterests(), request2.getInterests());
                            isMatched = true;
                        }
                    }

                    if (isMatched) {
                        log.info("매칭 유저 발견: {} - {} (대기시간 1분 초과: {}, {}, 매칭된 관심사: {}",
                                request1.getUserId(),
                                request2.getUserId(),
                                isRequest1TimeExpired,
                                isRequest2TimeExpired,
                                matchedInterests != null ? String.join(", ", matchedInterests) : "없음");

                        //관심사 없는 경우 처리
                        if (matchedInterests == null) {
                            matchedInterests = new ArrayList<>();
                            matchedInterests.add("0");
                        }

                        log.info("matchedInterest: {}", matchedInterests);

                        // 세션 및 토큰 생성 후 웹소켓을 통한 데이터 전송
                        createMatch(request1, request2, matchedInterests);
                        matchedRequests.addAll(Arrays.asList(jsonRequest1, jsonRequest2));
                        break;
                    }
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException("❌ 매칭 처리 중 오류가 발생했습니다.", e);
            }
        }

        // 매칭된 요청 대기열에서 제거
        for (String matchedRequest : matchedRequests) {
            log.info("매치 대기열에서 삭제 요청: {}", matchedRequest);
            removeFromQueue(matchedRequest);
        }
    }

    // 매칭 조건 1: 모드, 성별, 1개 이상의 공통 관심사 일치
    private boolean matchCondition1(MatchRequestDto request1, MatchRequestDto request2) {
<<<<<<< HEAD
        return request1.getModeId().equals(request2.getModeId()) &&
                request1.getGender().equals(request2.getTargetGender()) &&
                request1.getTargetGender().equals(request2.getGender());
=======
        log.info("매칭 조건1 확인 중...");
        log.info("request1: {}", request1.toString());
        log.info("request2: {}", request2.toString());

        // 두 명의 모드가 일치한지 확인
        if (!request1.getModeId().equals(request2.getModeId())) {
            return false;
        }

        log.info("조건 충족: 내모드: {}, 상대모드: {}", request1.getModeId(), request2.getModeId());

        // 두 명의 모드가 일치할 때
        Character myGender = request1.getGender();
        Character myTarget = request1.getTargetGender();
        Character otherGender = request1.getGender();
        Character otherTarget = request1.getTargetGender();

        // 성별 조건 탐색
        if (myGender.equals('F')) {
            if (myTarget.equals('M')) {
                // 여자가 타겟을 M으로 했을 때: 상대의 성별이 M이고, 상대의 타겟 성별이 F 또는 A
                if (otherGender.equals('M') && (otherTarget.equals('F') || otherTarget.equals('A'))) {
                    log.info("조건 충족: 내성별: {}, 내타겟: {}, 상대성별: {}, 상대타겟: {}",
                            myGender, myTarget, otherGender, otherTarget);
                    return true;
                }
            } else if (myTarget.equals('F')) {
                // 여자가 타겟을 F으로 했을 때: 상대의 성별이 F이고, 상대의 타겟 성별이 F 또는 A
                if (otherGender.equals('F') && (otherTarget.equals('F') || otherTarget.equals('A'))) {
                    log.info("조건 충족: 내성별: {}, 내타겟: {}, 상대성별: {}, 상대타겟: {}",
                            myGender, myTarget, otherGender, otherTarget);
                    return true;
                }
            } else if (myTarget.equals('A')) {
                // 여자가 타겟을 A으로 했을 때: 상대의 타겟 성별이 F 또는 A
                if (otherTarget.equals('F') || otherTarget.equals('A')) {
                    log.info("조건 충족: 내성별: {}, 내타겟: {}, 상대성별: {}, 상대타겟: {}",
                            myGender, myTarget, otherGender, otherTarget);
                    return true;
                }
            }
        } else if (myGender.equals('M')) {
            if (myTarget.equals('F')) {
                // 남자가 타겟을 F으로 했을 때: 상대의 성별이 F이고, 상대의 타겟 성별이 M 또는 A
                if (otherGender.equals('F') && (otherTarget.equals('M') || otherTarget.equals('A'))) {
                    log.info("조건 충족: 내성별: {}, 내타겟: {}, 상대성별: {}, 상대타겟: {}",
                            myGender, myTarget, otherGender, otherTarget);
                    return true;
                }
            } else if (myTarget.equals('M')) {
                // 남자가 타겟을 M으로 했을 때: 상대의 성별이 M이고, 상대의 타겟 성별이 M 또는 A
                if (otherGender.equals('M') && (otherTarget.equals('M') || otherTarget.equals('A'))) {
                    log.info("조건 충족: 내성별: {}, 내타겟: {}, 상대성별: {}, 상대타겟: {}",
                            myGender, myTarget, otherGender, otherTarget);
                    return true;
                }
            } else if (myTarget.equals('A')) {
                // 남자가 타겟을 A으로 했을 때: 상대의 타겟 성별이 M 또는 A
                if (otherTarget.equals('M') || otherTarget.equals('A')) {
                    log.info("조건 충족: 내성별: {}, 내타겟: {}, 상대성별: {}, 상대타겟: {}",
                            myGender, myTarget, otherGender, otherTarget);
                    return true;
                }
            }
        }

        return false;
>>>>>>> develop
    }

    // 매칭 조건 2: 1분 지남, 매칭 조건 상관 없음
    private boolean matchCondition2(MatchRequestDto request1, MatchRequestDto request2) {
        return true;
    }

    // 관심사가 1개 이상 존재하는지 확인
    private List<String> findCommonInterest(List<String> interests1, List<String> interests2) {
        return interests1.stream()
                .filter(interests2::contains)
                .toList();
    }

    // 대기열에서 특정 JSON 제거
    private void removeFromQueue(String jsonRequest) {
        redisTemplate.opsForList().remove(MATCH_QUEUE_KEY, 1, jsonRequest);
        log.info("매칭 대기열에서 삭제되었습니다.");
    }

    // 매칭 실패시 작업
    private void matchingFail(MatchRequestDto request1, MatchRequestDto request2, User user1, User user2) {
        // TODO추가
        log.info("매칭 실패 작업 진행");
    }

    // 매칭 생성
    @Transactional
    protected void createMatch(MatchRequestDto request1, MatchRequestDto request2, List<String> matchedInterests) {
        try {
<<<<<<< HEAD
=======
            // 매칭 전 티켓 확인
            User user1 = userRepository.findById(request1.getUserId())
                    .orElseThrow(() -> new RuntimeException("사용자1을 찾을 수 없습니다."));
            User user2 = userRepository.findById(request2.getUserId())
                    .orElseThrow(() -> new RuntimeException("사용자2를 찾을 수 없습니다."));

            // 티켓 수량 확인
            if (user1.getTicketQuantity() < 1 || user2.getTicketQuantity() < 1) {
                // 매칭 실패 작업 진행
                matchingFail(request1, request2, user1, user2);
                throw new RuntimeException("티켓이 부족합니다.");
            }

>>>>>>> develop
            // OpenVidu 세션 저장
            String sessionId = null;
            int maxRetries = 3; // 최대 재시도 수
            int retryCount = 0; // 재시도 수
            int retryDelayMs = 1000;    // 재시도 딜레이 (1초)

            while (sessionId == null && retryCount < maxRetries) {
                try {
                    sessionId = openViduService.createSession();
                    if (sessionId == null) {
                        retryCount++;
                        if (retryCount < maxRetries) {
                            log.warn("OpenVidu 세션 생성 실패 (시도 {}/{}), {}ms 후 재시도...",
                                    retryCount, maxRetries, retryDelayMs);
                            Thread.sleep(retryDelayMs);
                        }
                    }
                } catch (Exception e) {
                    retryCount++;
                    if (retryCount < maxRetries) {
                        log.warn("OpenVidu 세션 생성 중 오류 발생 (시도 {}/{}): {}",
                                retryCount, maxRetries, e.getMessage());
                        Thread.sleep(retryDelayMs);
                    }
                }
            }
            if (sessionId == null) {
                log.error("OpenVidu 세션 생성 최대 시도 횟수 초과 ({} 회)", maxRetries);
                throw new RuntimeException("❌ OpenVidu 세션 생성 실패");
            }

            JsonNode jsonNode = objectMapper.readTree(sessionId);
            String openviduSessionId = jsonNode.get("sessionId").asText();

            // 매칭된 유저 목록
            List<Integer> matchedUsers = Arrays.asList(
                    request1.getUserId(),
                    request2.getUserId()
            );

            // DB에 매칭 기록 저장
            Matching matching = Matching.builder()
                    .matchingModeId(request1.getModeId())
                    .state("진행중")
                    .dialogue("{\"none\": \"none\"}")
                    .interestId(Integer.parseInt(matchedInterests.get(0)))
                    .createdAt(LocalDateTime.now())
                    .build();

            Matching savedMatching = matchingRepository.save(matching);
            log.info("매칭 테이블에 저장 완료");

            // 매칭 유저 저장
            LocalDateTime time = LocalDateTime.now();
            MatchingUser matchingUser1 = MatchingUser.builder()
                    .matchingId(savedMatching)
                    .userId(request1.getUserId())
                    .userAvatar(request1.getUserAvatarId())
                    .rateeId(request2.getUserId())
                    .ratingContent("null")
                    .ratingScore((byte)100)
                    .startAt(time)
                    .endAt(time)
                    .build();

            MatchingUser matchingUser2 = MatchingUser.builder()
                    .matchingId(savedMatching)
                    .userId(request2.getUserId())
                    .userAvatar(request2.getUserAvatarId())
                    .rateeId(request1.getUserId())
                    .ratingContent("null")
                    .ratingScore((byte)100)
                    .startAt(time)
                    .endAt(time)
                    .build();

            matchingUserRepository.save(matchingUser1);
            matchingUserRepository.save(matchingUser2);
            log.info("매칭 유저 테이블에 각 유저 정보 저장 완료. {} - {}", matchingUser1.getUserId(), matchingUser2.getUserId());

            // 세션 정보 저장
            redisTemplate.opsForHash().put(MATCH_SESSION_KEY, openviduSessionId, String.join(",", matchedUsers.toString()));
            log.info("✅ 세션 정보 저장. 세션아이디: {}, 매칭된 유저: {}", openviduSessionId, matchedUsers);

<<<<<<< HEAD
            // TODO: 토큰 생성 추가 필요
            // WebSocket을 통해 세션 정보와 토큰 전송
            for (Integer matchedUser : matchedUsers) {
                log.info("\uD83C\uDF89 매칭 완료! 세션 ID: {}, 유저: {}", openviduSessionId, matchedUser);
                webSocketHandler.notifyMatch(matchedUser, openviduSessionId);
//                matchStompController.notifyMatch(matchedUser,openviduSessionId);
            }

=======
            // WebSocket을 통해 세션 정보와 각자의 토큰 전송
            for (int i = 0; i < matchedUsers.size(); i++) {
                Integer matchedUser = matchedUsers.get(i);
                String userToken = tokens.get(i); // 각 유저의 개별 토큰

                // JsonNode에서 token 값만 추출
                JsonNode tokenNode = objectMapper.readTree(userToken);
                String openViduToken = tokenNode.get("token").asText();

                // 내 닉네임 가져오기
                String nickname = (i == 0) ? user1.getNickname() : user2.getNickname();

                // 내 소개 가져오기
                String introduction = (i == 0) ? user1.getIntroduction() : user2.getIntroduction();

                log.info("매칭 완료! 세션 ID: {}, 유저: {}, 토큰: {}, 내 닉네임: {}, 내 소개 {}",
                        openviduSessionId, matchedUser, openViduToken, nickname, introduction);

                // 세션 ID(공통)와 토큰(개별), 상대방 닉네임 전송
                Map<String, Object> messageMap = new HashMap<>();
                messageMap.put("type", "OPENVIDU_SESSION");
                messageMap.put("sessionId", openviduSessionId);
                messageMap.put("token", openViduToken);
                messageMap.put("nickname", nickname);
                messageMap.put("introduction", introduction);
                messageMap.put("serverTime", System.currentTimeMillis());

                String message = objectMapper.writeValueAsString(messageMap);
                webSocketHandler.notifyMatch(matchedUser, message);
            }

            Integer userId1 = request1.getUserId();
            Integer userId2 = request2.getUserId();
            Integer modeId = request1.getModeId();
            // 스몰톡 미션(13)
            missionService.updateSmalltalk(userId1);
            missionService.updateSmalltalk(userId2);

            // 모드에 따른 미션 업데이트
            switch (modeId) {
                case 5: // 마이크패스 모드
                    log.info("마이크패스 모드 미션 업데이트 - userId1: {}, userId2: {}", userId1, userId2);
                    missionService.updateSmalltalkmodeMike(userId1);
                    missionService.updateSmalltalkmodeMike(userId2);
                    break;
                case 6: // 자유 모드
                    log.info("자유 모드 미션 업데이트 - userId1: {}, userId2: {}", userId1, userId2);
                    missionService.updateSmalltalkmodeFree(userId1);
                    missionService.updateSmalltalkmodeFree(userId2);
                    break;
            }

            // 성별 'A' 선택한 경우 추가 미션 업데이트
            if (request1.getTargetGender() == 'A') {
                log.info("성별 모두 선택 미션 업데이트 - userId: {}", userId1);
                missionService.updateSmalltalkAll(userId1);
            }
            if (request2.getTargetGender() == 'A') {
                log.info("성별 모두 선택 미션 업데이트 - userId: {}", userId2);
                missionService.updateSmalltalkAll(userId2);
            }
            // 티켓 차감
            int beforeTicketQuantity1 = user1.getTicketQuantity();
            int beforeTicketQuantity2 = user2.getTicketQuantity();

            user1.setTicketQuantity(beforeTicketQuantity1 - 1);
            user2.setTicketQuantity(beforeTicketQuantity2 - 1);

            userRepository.save(user1);
            userRepository.save(user2);

            // 티켓 사용 이력 생성
            assetHistoryService.createTicketHistory(
                    user1,
                    -1,
                    beforeTicketQuantity1,
                    beforeTicketQuantity1 - 1,
                    0,
                    "스몰톡 매칭");

            assetHistoryService.createTicketHistory(
                    user2,
                    -1,
                    beforeTicketQuantity2,
                    beforeTicketQuantity2 - 1,
                    0,
                    "스몰톡 매칭");

            log.info("매칭 생성 완료");
>>>>>>> develop
        } catch (Exception e) {
            log.error("매칭 생성 중 오류 발생", e);
            throw new RuntimeException("매칭 생성 실패", e);
        }
    }

    /**
     * 3️⃣ 매칭 종료 및 MySQL 저장
     */
    public ResponseEntity<?> cancelMatch(Integer userId) {
        log.info("매칭 요청 취소 요청. userId: {}", userId);

        // 대기열 존재 확인
        List<String> queues = redisTemplate.opsForList().range(MATCH_QUEUE_KEY, 0, -1);
        if (queues == null) {
            log.info("대기열이 비어있습니다.");
            return ResponseEntity.ok(Map.of("message", "대기열이 존재하지 않습니다."));
        }

        // 유저 찾기
        for (String queue : queues) {
            try {
                MatchRequestDto request = objectMapper.readValue(queue, MatchRequestDto.class);

                // 유저 찾으면
                if (request.getUserId().equals(userId)) {
                    // 매칭 대기열에서 삭제
                    removeFromQueue(queue);

                    // 웹소켓 연결 해제
<<<<<<< HEAD
                    //webSocketHandler.cancelRequestMatch(userId);
                    stompHandler.unregisterSession(userId);
                    return ResponseEntity.ok(Map.of("message", "정상적으로 매칭 요청이 취소되었습니다."));
=======
                    webSocketHandler.cancelRequestMatch(userId);
                    // stompHandler.unregisterSession(userId);
                    return ResponseEntity.ok("정상적으로 매칭 요청이 취소되었습니다.");
>>>>>>> develop
                }
            } catch (JsonProcessingException e) {
                log.error("매칭 요청 취소 중 JSON 처리 오류 발생", e);
                ResponseEntity.ok(Map.of("message", "매칭 요청 취소 중 JSON 처리 오류 발생"));
            } catch (Exception e) {
                log.error("매칭 요청 취소 중 오류 발생", e);
                return ResponseEntity.ok(Map.of("message", "매칭 요청 취소 중 오류 발생"));
            }
        }

        log.info("매칭 대기열에서 해당 유저를 찾을 수 없습니다. userId: {}", userId);
        return ResponseEntity.ok(Map.of("message", "매칭 대기열에서 해당 유저를 찾을 수 없습니다."));
    }

    private Boolean deleteUserFromQueue(Integer userId) {
        log.info("매칭 대기열에서 유저 {} 확인 후 삭제 요청", userId);
        try {
<<<<<<< HEAD
            ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
=======
            // 1. 매칭 대기열 가져오기
            List<String> queues = redisTemplate.opsForList().range(MATCH_QUEUE_KEY, 0, -1);
            if (queues == null) {
                log.info("대기열이 비어있습니다.");
                return false;
            }

            // 2. 현재 유저가 존재하는지 확인
            for (String queue : queues) {
                MatchRequestDto request = objectMapper.readValue(queue, MatchRequestDto.class);

                // 2-1. 존재하면 대기열에서 제거 후 종료
                if (request.getUserId().equals(userId)) {
                    log.info("매칭 대기열에서 해당 유저를 찾았습니다. userId: {}", userId);
                    removeFromQueue(queue);
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("매칭 대기열 유저 삭제중 오류 발생");
            return false;
        }

        return false;
    }

    // 모든 웹소켓 끊김 활동에 대해서 작동함, 대기열 중간에 탈주하거나 매칭 중간에 탈주할 경우 실행 됨
    public void deleteMatch(Integer userId) {
        try {
            if (!deleteUserFromQueue(userId)) {
                log.info("매칭 대기열이 비어있거나 해당 유저가 존재하지 않음. userId: {}", userId);
            } else {
                log.info("매칭 대기열에 해당 유저가 존재하여 삭제 하였음. userId: {}");
            }

            // 3. 세션 정보 가져오기
>>>>>>> develop
            HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();

<<<<<<< HEAD
            // 대기열에서 삭제
            zSetOps.remove(MATCH_QUEUE_KEY, userId);

            // 해당 WebSocket과 연결된 매칭 조회
            Map<String, String> allSessions = hashOps.entries(MATCH_SESSION_KEY);

            String targetSessionId = null;
            List<Integer> sessionUsers = null;

            for (Map.Entry<String, String> entry : allSessions.entrySet()) {
                String sessionId = entry.getKey();
                String[] users = entry.getValue().split(",");
                log.info("🔍 세션 정보: {} => {}", sessionId, Arrays.toString(users));

                if (Arrays.asList(users).contains(userId.toString())) {
                    targetSessionId = sessionId;
                    sessionUsers = Arrays.stream(users)
                            .map(Integer::parseInt)
                            .collect(Collectors.toList());
=======
            // 4. 현재 유저가 존재하는 세션 검색, 정상종료로 인한 사람은 세션 정보가 없기에 검색되지 않을것
            for (Map.Entry<String, String> entry : sessions.entrySet()) {
                String sessionId = entry.getKey();
                String value = entry.getValue();

                String[] parts = value.split(":");
                String matchingId = parts[0];

                List<Integer> users = Arrays.stream(parts[1].replaceAll("[\\[\\]]", "").split(","))
                        .map(String::trim)
                        .map(Integer::parseInt)
                        .toList();

                // 4-1. 존재하면
                if (users.contains(userId)) {
                    log.info("현재 유저가 존재하는 세션을 찾았습니다. sessionId: {}", sessionId);

                    // 4-2-1. 세션 정보 삭제
                    redisTemplate.opsForHash().delete(MATCH_SESSION_KEY, sessionId);

                    // 4-2-2. 매칭 정보 업데이트 ("비정상종료") 으로 수정
                    Matching matching = matchingRepository.findById(Integer.parseInt(matchingId))
                            .orElseThrow(() -> new RuntimeException("매칭 정보를 찾을 수 없습니다."));

                    matching = matching.toBuilder()
                            .state("비정상종료")
                            .endedAt(LocalDateTime.now())
                            .build();

                    matchingRepository.save(matching);

                    try {
                        // 4-2-3. openvidu 세션 제거
                        // openViduService.closeSession(sessionId);
                        log.info("OpenVidu 세션 종료 완료");
                    } catch (Exception e) {
                        log.error("OpenVidu 세션 종료 중 오류 발생: {}", e.getMessage());
                    }

                    // 4-2-4. 세션 종료 알림 보내기
                    log.info("세션 종료 알림 보내는 중...");
                    for (Integer user : users) {
                        webSocketHandler.notifyMatch(user, "{\"type\":\"MATCH_SESSION_CLOSED\"}");
                    }

>>>>>>> develop
                    break;
                }
            }

            if (targetSessionId != null && sessionUsers != null) {
                try {
                    // OpenVidu 세션 종료 시도
                    try {
                        openViduService.closeSession(targetSessionId);
                        log.info("✅ OpenVidu 세션 종료: {}", targetSessionId);
                    } catch (Exception e) {
                        log.info("ℹ️ OpenVidu 세션이 이미 종료됨: {}", targetSessionId);
                    }

                    // Redis에서 세션 정보 삭제
                    hashOps.delete(MATCH_SESSION_KEY, targetSessionId);
                    log.info("✅ Redis 세션 정보 삭제: {}", targetSessionId);

                    // 세션에 있던 모든 유저에게 알림 및 STOMP 세션 강제 종료
                    for (Integer user : sessionUsers) {
                        // STOMP 세션 종료 알림
                        webSocketHandler.notifySessionClosed(user, "MATCH_SESSION_CLOSED");

                        // STOMP 세션 강제 종료
                        //webSocketHandler.forceDisconnect(user);

                        log.info("✅ 유저 연결 종료: {}", user);
                    }
                } catch (Exception e) {
                    log.error("❌ 세션 종료 처리 중 오류 발생: {}", e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("❌ 매칭 삭제 중 오류 발생: {}", e.getMessage());
        }
    }


    public List<MatchingModeResponseDto> getMatchMode() {
        List<MatchingMode> modes = matchingModeRepository.findAll();
        if (modes.isEmpty()) {
            return Collections.emptyList();
        }

        return modes.stream()
                .map(MatchingModeResponseDto::from)
                .toList();
    }

    public ResponseEntity<?> getMatchHistory(LocalDate date, Integer page, Integer userId) {
        try {
<<<<<<< HEAD
            userId = 5;
            PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "startAt"));
=======
            PageRequest pageRequest = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "startAt"));
>>>>>>> develop

            // LocalDate를 LocalDateTime으로 변환
            LocalDateTime searchDate = date.atStartOfDay();

            log.info("매칭 기록 조회 시작: date={}, userId={}, page={}", date, userId, page);
            Page<MatchHistoryResponseDto> matchHistory = matchingRepository.findMatchHistoryByUserIdAndDate(
                    userId,
                    searchDate,
                    pageRequest
            );

            if (matchHistory.isEmpty()) {
                log.info("매칭 기록이 없습니다. userId: {}, date: {}", userId, date);
                return ResponseEntity.ok(new PageResponseDto<>(Page.empty()));
            }

            log.info("매칭 기록 조회 완료: {} 건", matchHistory.getTotalElements());
            return ResponseEntity.ok(new PageResponseDto<>(matchHistory));
        } catch (Exception e) {
            log.error("매칭 기록 조회 중 오류 발생", e);
            return ResponseEntity.internalServerError().body("매칭 기록 조회 중 오류가 발생했습니다.");
        }
    }

<<<<<<< HEAD
=======
    public ResponseEntity<?> getMatchPartner(Integer userId) {
        try {
            // 1. 매칭 상대 찾기
            Map.Entry<String, List<String>> userList = sessionService.getSessionByUserId(userId);
            if (userList == null) {
                log.error("매칭 정보가 존재하지 않습니다.");
                return ResponseEntity.badRequest().body("매칭 정보가 존재하지 않습니다.");
            }

            // 2. 매칭 정보 조회
            List<String> partnerList = userList.getValue();
            if (partnerList.isEmpty() || partnerList.size() != 2) {
                log.error("매칭 상대 정보가 존재하지 않습니다.");
                return ResponseEntity.badRequest().body("매칭 상대 정보가 존재하지 않습니다.");
            }

            // 3. 매칭 상대 아이디 조회
            String partnerId = partnerList.get(0).equals(userId) ? partnerList.get(1) : partnerList.get(0);
            if (partnerId == null) {
                log.error("매칭 상대 정보가 존재하지 않습니다.");
                return ResponseEntity.badRequest().body("매칭 상대 정보가 존재하지 않습니다.");
            }

            // 4. 매칭 상대 정보 조회
            User partner = userRepository.findById(Integer.parseInt(partnerId))
                    .orElseThrow(() -> new RuntimeException("매칭 상대를 찾을 수 없습니다."));

            if (partner == null) {
                log.error("매칭 상대 정보가 존재하지 않습니다.");
                return ResponseEntity.badRequest().body("매칭 상대 정보가 존재하지 않습니다.");
            }

            MatchPartnerDTO matchPartnerDTO = MatchPartnerDTO.builder()
                    .nickName(partner.getNickname())
                    .introduction(partner.getIntroduction())
                    .build();

            return ResponseEntity.ok(matchPartnerDTO);
        } catch (Exception e) {
            log.error("매칭 상대 정보 조회 중 오류 발생", e);
            return ResponseEntity.internalServerError().body("매칭 상대 정보 조회 중 오류가 발생했습니다.");
        }
    }

    public ResponseEntity<?> getMatchTopic(Integer userId) {
        try {
            // 1. 현재 유저가 포함된 세션을 통해 매칭 아이디 찾기
            String matchingId = sessionService.findMatchingIdByUserId(userId);

            // 2. 매칭 아이디에서 매칭 주제 ID를 가져옴
            Matching matching = matchingRepository.findById(Integer.parseInt(matchingId))
                    .orElseThrow(() -> new RuntimeException("매칭 정보를 찾을 수 없습니다."));

            // 3. 관심사 ID로 TopicInterest 목록을 조회하고, Topic의 name을 추출
            List<String> topics = topicInterestRepository.findByIdInterestId(matching.getInterestId())
                    .stream()
                    .map(topicInterest -> topicInterest.getTopic().getName())
                    .collect(Collectors.toList());

            if (topics.isEmpty()) {
                log.warn("매칭된 관심사의 주제가 없습니다. interestId: {}", matching.getInterestId());
                return ResponseEntity.ok(Collections.emptyList());
            }

            log.info("매칭 주제 조회 완료. topics: {}", topics);
            return ResponseEntity.ok(topics);

        } catch (Exception e) {
            log.error("매칭중 관심사 주제를 가져오는데 오류 발생", e);
            return ResponseEntity.badRequest().body("관심사 가져오는 중 오류 발생");
        }
    }

    @Transactional
    public ResponseEntity<?> extendMatch(Integer userId) {
        log.info("매칭 연장 처리 요청 됨, 요청자: {}", userId);

        try {
            // 1. 현재 유저가 포함된 세션 정보 가져오기
            SessionInfoDTO sessionInfo = sessionService.findSessionInfoByUserId(userId);
            if (sessionInfo == null) {
                log.error("세션 정보가 존재하지 않습니다");
                return ResponseEntity.badRequest().body("해당 유저가 존재하는 세션을 찾을 수 없습니다");
            }

            // 2. 해당 유저의 코인 감소
            log.info("{} 의 매칭 연장으로 코인 1 감소 요청", userId);
            assetHistoryService.insertCoin(userId, Integer.parseInt(sessionInfo.getMatchingId()));

            // 3. 해당 유저의 매칭 유저 테이블 extend 정보 갱신
            MatchingUser matchingUser1 = matchingUserRepository
                    .findByMatchingId_MatchingIdAndUserId(Integer.parseInt(sessionInfo.getMatchingId()), userId);
            MatchingUser updateMatchingUser1 = matchingUser1.toBuilder()
                    .extend(true)
                    .build();

            // 4. 해당 상대 유저의 매칭 유저 테이블 accept 정보 갱신
            MatchingUser matchingUser2 = matchingUserRepository.findByMatchingId_MatchingIdAndUserId(
                    Integer.parseInt(sessionInfo.getMatchingId()),
                    sessionInfo.getUserList().stream().filter(id -> !id.equals(userId)).findFirst()
                            .orElseThrow(() -> new RuntimeException("상대방 userId를 찾을 수 없습니다")));
            MatchingUser updateMatchingUser2 = matchingUser2.toBuilder()
                    .accept(true)
                    .build();

            // 5. 매칭 테이블 state 값 변경 "진행중연장"
            Matching matching = matchingRepository.findById(Integer.parseInt(sessionInfo.getMatchingId()))
                    .orElseThrow(
                            () -> new RuntimeException("매칭 정보를 찾을 수 없습니다. matchingId: " + sessionInfo.getMatchingId()));
            Matching updateMatching = matching.toBuilder()
                    .state("진행중연장")
                    .build();

            log.info("매칭 연장 정보 저장 중...");
            matchingUserRepository.save(updateMatchingUser1);
            matchingUserRepository.save(updateMatchingUser2);
            matchingRepository.save(updateMatching);
            log.info("매칭 연장 정보 저장 완료");

            return ResponseEntity.ok("매칭 연장 처리 되었습니다.");
        } catch (RuntimeException e) {
            log.error("데이터 조회 중 오류 발생", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("매칭 연장 처리 중 오류 발생. ", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    /**
     * 매칭 종료
     *
     * 이 메서드가 호출 되었다는 것은 매칭이 정상적으로 종료 되었다는 것
     *
     * 어느 한쪽이 먼저 누르든 상관없음
     * 누르는 순간 양쪽의 데이터를 업데이트 함
     *
     * @param userId
     * @return
     */
    @Transactional
    public ResponseEntity<?> endMatch(Integer userId) {
        log.info("매칭이 종료 처리 요청됨. userId: {}", userId);
        try {
            // 1. 해당 유저가 포함된 세션 정보를 가져옴
            SessionInfoDTO session = sessionService.findSessionInfoByUserId(userId);
            if (session == null) {
                log.info("이미 종료된 세션입니다.");
                return ResponseEntity.ok("매칭이 종료되었습니다.");
            }

            // 2. 세션을 레디스에서 삭제
            log.info("MATCH_SESSIO에서 해당 세션 삭제, sessionId: {}", session.getSessionId());
            sessionService.deleteSession(session.getSessionId());

            // 3. 매칭의 상태 "정상종료"로 변경, endAt 수정, "진행중연장"이면 "연장종료"
            Matching matching = matchingRepository.findById(Integer.parseInt(session.getMatchingId()))
                    .orElseThrow(() -> new RuntimeException("매칭 정보를 찾을 수 없습니다"));

            Matching updatedMatching = matching.toBuilder()
                    .state(matching.getState().equals("진행중연장") ? "연장종료" : "정상종료")
                    .endedAt(LocalDateTime.now())
                    .build();

            matchingRepository.save(updatedMatching);

            // 4. 세션 종료 알림 보냄
            for (Integer user : session.getUserList()) {
                log.info("세션 종료 알림 보냄. userId: {}", user);
                webSocketHandler.notifyMatch(user, "{\"type\":\"MATCH_SESSION_CLOSED\"}");
            }

            // 5. 세션 종료 시킴
            try {
                log.info("openvidu 세션 종료 요청");
                openViduService.closeSession(session.getSessionId());
            } catch (Exception e) {
                log.info("이미 종료된 세션이거나 세션 종료중 오류 발생, {} - {}", session.getSessionId(), session.getUserList());
                // return ResponseEntity.ok("이미 종료된 세션입니다");
            }

            log.info("정상적으로 매칭 종료 완료");
            return ResponseEntity.ok("정상적으로 종료되었습니다.");
        } catch (RuntimeException e) {
            log.info("매칭  데이터 처리중 오류 발생. {}", e.getMessage());
            return ResponseEntity.badRequest().body("매칭 데이터 처리중 오류 발생");
        } catch (Exception e) {
            log.error("매칭 종료 처리중 오류 발생. {}", e.getMessage());
            return ResponseEntity.badRequest().body("매칭 종료 처리중 오류 발생");
        }
    }

    public ResponseEntity<?> extendMatch_notUsed(Integer userId, Boolean accept) {
        try {
            // 1. 현재 유저가 포함된 세션 찾기
            Map.Entry<String, List<String>> userSession = sessionService.getSessionByUserId(userId);
            if (userSession == null) {
                log.error("매칭 세션을 찾을 수 없습니다. userId: {}", userId);
                return ResponseEntity.badRequest().body("매칭 세션을 찾을 수 없습니다.");
            }

            String sessionId = userSession.getKey();
            List<String> users = userSession.getValue();

            // 2. Redis에 연장 응답 저장 (Hash 사용)
            String extendResponseKey = MATCH_EXTEND_KEY + ":" + sessionId;
            redisTemplate.opsForHash().put(extendResponseKey, userId.toString(), accept.toString());

            // 3. 두 유저의 응답이 모두 있는지 확인
            Map<Object, Object> responses = redisTemplate.opsForHash().entries(extendResponseKey);
            if (responses.size() == 2) {
                // 4. 두 유저 모두 수락했는지 확인
                boolean allAccepted = responses.values().stream()
                        .allMatch(response -> response.toString().equals("true"));

                Map<String, Object> messageMap = new HashMap<>();
                messageMap.put("type", "MATCH_EXTEND_RESULT");

                if (allAccepted) {
                    messageMap.put("result", "MATCH_EXTEND_ACCEPT");
                    log.info("매칭 연장이 수락되었습니다. sessionId: {}", sessionId);
                } else {
                    messageMap.put("result", "MATCH_EXTEND_REJECT");
                    log.info("매칭 연장이 거절되었습니다. sessionId: {}", sessionId);
                }

                messageMap.put("result", allAccepted ? "MATCH_EXTEND_ACCEPT" : "MATCH_EXTEND_REJECT");
                messageMap.put("serverTime", System.currentTimeMillis());

                // 5. 두 유저에게 결과 전송
                String message = objectMapper.writeValueAsString(messageMap);
                for (String user : users) {
                    webSocketHandler.notifyMatch(Integer.parseInt(user), message);
                }

                // 6. Redis에서 연장 응답 정보 삭제
                redisTemplate.delete(extendResponseKey);

                // 7. 거절된 경우 세션 정보도 삭제
                if (!allAccepted) {
                    // 매칭 상태를 "종료됨"으로 업데이트
                    String matchingId = sessionService.findMatchingIdByUserId(userId);
                    if (matchingId != null) {
                        Matching matching = matchingRepository.findById(Integer.parseInt(matchingId))
                                .orElseThrow(() -> new RuntimeException("매칭 정보를 찾을 수 없습니다."));

                        matching = matching.toBuilder()
                                .state("종료됨")
                                .build();

                        matchingRepository.save(matching);
                    }

                    // Redis에서 세션 정보 삭제
                    redisTemplate.opsForHash().delete(MATCH_SESSION_KEY, sessionId);
                }

                return ResponseEntity.ok(messageMap.get("result"));
            }

            // 아직 상대방의 응답을 기다리는 중
            return ResponseEntity.ok("WAITING_FOR_PARTNER");

        } catch (Exception e) {
            log.error("매칭 연장 처리 중 오류 발생", e);
            return ResponseEntity.internalServerError().body("매칭 연장 처리 중 오류가 발생했습니다.");
        }
    }

    // 특정 월의 매칭 날짜들 조회
    public ResponseEntity<?> getHistoryCount(Integer userId, LocalDate date) {
        try {
            // 시작일 마지막일 계산
            LocalDateTime startOfMonth = date.withDayOfMonth(1).atStartOfDay();
            LocalDateTime endOfMonth = date.withDayOfMonth(date.lengthOfMonth()).atTime(23, 59, 59);

            log.info("월별 매칭 날짜 조회 시작: userId: {}, 시작일: {}, 종료일: {}", userId, startOfMonth, endOfMonth);

            // 날짜 조회 시작
            List<String> matchDates = matchingUserRepository.findDistinctMatchingDatesByUserIdAndPeriod(
                    userId,
                    startOfMonth,
                    endOfMonth);

            if (matchDates.isEmpty()) {
                log.info("해당 월에 매칭 기록이 없습니다. userId: {}, 월: {}", userId, date);
                return ResponseEntity.ok(Collections.emptyList());
            }

            log.info("월별 매칭 기록 조회 완료. {} 건", matchDates.size());
            MatchDatesResponseDTO response = new MatchDatesResponseDTO(matchDates, matchDates.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("월별 매칭 날짜 조회 중 오류 발생", e);
            return ResponseEntity.internalServerError().body("월별 매칭 날짜 조회 중 오류가 발생했습니다.");
        }
    }

>>>>>>> develop
    @EventListener
    public void handleWebSocketDisconnect(WebSocketDisconnectEvent event) {
        deleteMatch(event.getUserId());
    }


}

// /**
// * 1️⃣ 매칭 요청 (Redis에 유저 추가 & 자동 매칭)
// * DONE: WebSocket을 통신 관련 추가 필요
// */
// public String requestMatch(String userId, double score) throws Exception {
// ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
// Set<String> candidates = zSetOps.rangeByScore(MATCH_QUEUE_KEY, score - 50,
// score + 50);
//
// System.out.println("🔍 매칭 요청: " + userId + " [점수: " + score + "]");
// if (candidates != null) {
// System.out.println("🔍 대기 중인 유저: " + candidates);
// }
// if (candidates != null && candidates.size() >= GROUP_SIZE) {
// System.out.println("🎉 매칭 성공!");
// List<String> matchedUsers = new ArrayList<>(candidates).subList(0,
// GROUP_SIZE);
// for (String matchedUser : matchedUsers) {
// zSetOps.remove(MATCH_QUEUE_KEY, matchedUser);
// }
// // 본인 추가
// matchedUsers.add(userId);
//
// // ✅ OpenVidu 세션 생성
// String sessionId = openViduService.createSession();
// if (sessionId == null) {
// System.out.println("❌ OpenVidu 세션 생성 실패, 재시도...");
// sessionId = openViduService.createSession();
//
// if (sessionId == null) {
// return "❌ OpenVidu 세션 생성 실패";
// }
// }
// System.out.println(sessionId);
//
// // ✅ Redis에 세션 저장
// // redisTemplate.opsForHash().put(MATCH_SESSION_KEY, sessionId,
// String.join(",", matchedUsers));
// // System.out.println("Openvidu 세션 저장: " + sessionId + ", 유저: " +
// matchedUsers);
// //redisTemplate.expire(MATCH_SESSION_KEY, MATCH_DURATION, TimeUnit.SECONDS);
//
// ObjectMapper objectMapper = new ObjectMapper();
// JsonNode jsonNode = objectMapper.readTree(sessionId);
// String openviduSessionId = jsonNode.get("sessionId").asText();
//
// // Redis에 세션 저장
// redisTemplate.opsForHash().put(MATCH_SESSION_KEY, openviduSessionId,
// String.join(",", matchedUsers));
// System.out.println("Openvidu 세션 저장: " + openviduSessionId + ", 유저: " +
// matchedUsers);
//
// // ✅ WebSocket을 통해 매칭 정보 전송
// for (String matchedUser : matchedUsers) {
// System.out.println("🎉 매칭 완료! 세션 ID: " + sessionId + "유저: " + matchedUser);
// webSocketHandler.notifyMatch(matchedUser, sessionId);
// }
//
// return "✅ 매칭 완료: " + matchedUsers + " [세션 ID: " + sessionId + "]";
// }
//
// // 대기열 추가
// zSetOps.add(MATCH_QUEUE_KEY, userId, score);
// return "⏳ 대기 중... " + userId + "이(가) 대기열에 추가됨";
// }

/**
 * 2️⃣ OpenVidu 세션 생성
 * private String createOpenViduSession() {
 * // OpenVidu API 호출하여 세션 생성
 * Map<String, Object> requestBody = new HashMap<>();
 * return UUID.randomUUID().toString(); // 임시 UUID 사용 (OpenVidu API 호출 필요)
 * /**
 * OpenVidu API 호출하여 세션 생성
 * https://docs.openvidu.io/en/stable/reference-docs/REST-API/#post-session 참고
 * POST를 통한 세션 생성
 *
 * webclient/
 * }
 */


//        // 대기열에 존재하는지 확인
//        // LocalDateTime endTime = LocalDateTime.now();
//        // String userIdsJson = String.join(",", users);
//        //
//        // MatchHistory matchHistory = MatchHistory.builder()
//        // .matchingModeId(1) // Set appropriate matchingModeId
//        // .state("ACT") // Set appropriate state
//        // .interestId(1) // Set appropriate interestId
//        // .dialogue(userIdsJson)
//        // .createdAt(startTime)
//        // .endedAt(endTime)
//        // .build();
//        //
//        // smallTalkUserRepository.save(matchHistory);
//        //
//        // redisTemplate.opsForHash().delete(MATCH_SESSION_KEY, sessionId);
//        // for (String user : users) {
//        // webSocketHandler.notifyMatch(user, "🚫 세션이 종료되었습니다.");
//        // }
//        // 웹소켓 연결 해제
//        System.out.println("🚫 매칭 취소: " + userId);
//        webSocketHandler.cancelRequestMatch(userId);
//
//        // 매칭 대기열에서 삭제
//        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
//        zSetOps.remove(MATCH_QUEUE_KEY, userId);
//        System.out.println("🚫 대기열에서 삭제: " + userId);
//public void deleteMatch(Integer userId) {
//    try {
//        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
//        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
//
//        // 대기열에서 삭제
//        zSetOps.remove(MATCH_QUEUE_KEY, userId);
//
//        // 해당 WebSocket과 연결된 매칭 조회
//        Map<String, String> allSessions = hashOps.entries(MATCH_SESSION_KEY);
//
//        String targetSessionId = null;
//        List<Integer> sessionUsers = null;
//
//        for (Map.Entry<String, String> entry : allSessions.entrySet()) {
//            String sessionId = entry.getKey();
//            String[] users = entry.getValue().split(",");
//            log.info("🔍 세션 정보: {} => {}", sessionId, Arrays.toString(users));
//
//            if (Arrays.asList(users).contains(userId.toString())) {
//                targetSessionId = sessionId;
//                sessionUsers = Arrays.stream(users)
//                        .map(Integer::parseInt)
//                        .collect(Collectors.toList());
//                break;
//            }
//        }
//
//        if (targetSessionId != null && sessionUsers != null) {
//            try {
//                // OpenVidu 세션 종료 시도
//                try {
//                    openViduService.closeSession(targetSessionId);
//                    System.out.println("✅ OpenVidu 세션 종료: " + targetSessionId);
//                } catch (Exception e) {
//                    System.out.println("ℹ️ OpenVidu 세션이 이미 종료됨: " + targetSessionId);
//                }
//
//                // Redis에서 세션 정보 삭제
//                hashOps.delete(MATCH_SESSION_KEY, targetSessionId);
//                System.out.println("✅ Redis 세션 정보 삭제: " + targetSessionId);
//
//                // 세션에 있던 모든 유저에게 알림 및 연결 종료
//                for (Integer user : sessionUsers) {
//                    //webSocketHandler.notifySessionClosed(user, "MATCH_SESSION_CLOSED");
//                    matchStompController.notifySessionClosed(user, "MATCH_SESSION_CLOSED");
//                    System.out.println("✅ 유저 연결 종료: " + user);
//                }
//            } catch (Exception e) {
//                System.out.println("❌ 세션 종료 처리 중 오류: " + e.getMessage());
//            }
//        }
//    } catch (Exception e) {
//        System.out.println("❌ 매칭 삭제 중 오류 발생: " + e.getMessage());
//        e.printStackTrace();
//    }
//}