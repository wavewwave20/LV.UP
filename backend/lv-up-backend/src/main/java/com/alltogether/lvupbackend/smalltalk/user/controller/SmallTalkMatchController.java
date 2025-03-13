package com.alltogether.lvupbackend.smalltalk.user.controller;

import com.alltogether.lvupbackend.mission.service.MissionService;
import com.alltogether.lvupbackend.smalltalk.user.dto.MatchRequestDto;
import com.alltogether.lvupbackend.smalltalk.user.dto.MatchingModeResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.alltogether.lvupbackend.smalltalk.user.service.MatchService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/smalltalk/user")
@Slf4j
@RequiredArgsConstructor
public class SmallTalkMatchController {

    private final MatchService matchService;
    private final MissionService missionService;

    // JWT로부터 사용자 아이디 조회
    private Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("현재 인증된 사용자가 없습니다.");
        }
        return Integer.parseInt(authentication.getName());
    }

    /**
     * 매칭 요청
     *
     * POST /api/smalltalk/user/match?modeId={modeId}&target={A}
     *
     * @param modeId 매칭 모드 아이디 (M = 남자, F = 여자, A = 모두)
     *
     * @return 매칭 요청 결과
     */
    @PostMapping("/match")
    public ResponseEntity<?> requestMatch(@RequestParam(value = "modeId") Integer modeId, @RequestParam(value = "target") String target) {
        log.info("매칭 요청됨, modeId: {}, target: {}", modeId, target);
        if (modeId == null || target == null) {
            log.error("매칭 요청 잘못됨");
            return ResponseEntity.ok("매칭 요청이 잘못되었습니다.");
        }

        Integer userId = getCurrentUserId();    // 유저 ID
        List<String> interests = Collections.emptyList();   // 유저 관심사
        Character gender = 'M'; // 유저 성별


        // 매칭 요청 정보 생성
        MatchRequestDto matchRequest = MatchRequestDto.builder()
                .userId(userId)
                .gender(gender)
                .interests(interests)
                .userAvatarId(0)
                .modeId(modeId)
                .targetGender(target.charAt(0))
                .timestamp(System.currentTimeMillis())
                .build();

        log.info("매칭 요청됨, userId: {}, gender: {}", userId, gender);
        ResponseEntity<?> response = matchService.addToMatchingQueue(matchRequest);
        // 일일 매칭 미션 업데이트
        // TODO: 미션에서 값 중복 오류 발생
<<<<<<< HEAD
        //missionService.updateMatchMission(getCurrentUserId());
=======
        try {
            missionService.updateSmalltalk(userId);
        } catch (Exception e) {
            log.error("일일 매칭 미션 업데이트 실패. userId: {}", userId);
            if (!response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.badRequest().body("일일 매칭 미션 업데이트 실패");
            }
        }
>>>>>>> develop
        log.info("일일 매칭 미션 업데이트 됨. {}", userId);

        return response;
    }

    /**
     * 매칭 수락
     *
     * POST /api/smalltalk/user/match/cancel?userId={userId}
     *
     * @return 매칭 수락 결과
     */
    @PostMapping("/match/cancel")
    public ResponseEntity<?> cancelMatch() {
        Integer userId = getCurrentUserId();
        return matchService.cancelMatch(userId);
    }


    /**
     * 매칭 모드 목록 조회
     *
     * GET /api/smalltalk/user/match/mode
     *
     * @return 매칭 모드 목록
     */
    @GetMapping("/match/mode")
    public ResponseEntity<?> getMatchMode() {
        List<MatchingModeResponseDto> result = matchService.getMatchMode();

        if (result.isEmpty()) {
            log.error("매칭 모드 목록 조회 실패");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 매칭 기록 조회
     *
     * GET /api/smalltalk/user/match/history?date={20250101}&page={page}
     *
     * @return 매칭 기록 리스트
     */
    @GetMapping("/match/history")
    public ResponseEntity<?> getMatchHistory(
            @RequestParam(value = "date", required = false) String dateStr,
            @RequestParam(value = "page", required = true, defaultValue = "0") Integer page) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date;

        try {
            date = (dateStr != null) ? LocalDate.parse(dateStr, dateTimeFormatter) : LocalDate.now();
        } catch (Exception e) {
            log.error("날짜 형식이 잘못되었습니다. dateStr: {}, 기본값(오늘)으로 설정합니다.", dateStr);
            date = LocalDate.now();
        }

        Integer userId = getCurrentUserId();
        return matchService.getMatchHistory(date, page, userId);
    }
<<<<<<< HEAD
=======

    // 특정 월에 대화 기록이 있는 날짜들 조회
    @GetMapping("/match/history/monthly")
    public ResponseEntity<?> getMatchHistoryCount(
            @RequestParam(value = "date", required = true) String dateStr
    ) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.parse(dateStr, dateTimeFormatter);
        Integer userId = getCurrentUserId();

        return matchService.getHistoryCount(userId, date);
    }

    /**
     * 매칭 상대 정보 조회
     *
     * GET /api/smalltalk/user/match/partner
     *
     * @return 매칭 상대 정보
     */
    @GetMapping("/match/partner")
    public ResponseEntity<?> getMatchPartner() {
        Integer userId = getCurrentUserId();

        log.info("매칭 상대 정보 요청함. usrId: {}", userId);
        return matchService.getMatchPartner(userId);
    }

    /**
     * 매칭 중 주제 추천
     *
     * GET /api/smalltalk/user/match/topic
     *
     * @return 추천된 주제 목록
     */
    @GetMapping("/match/topic")
    public ResponseEntity<?> getMatchTopic() {
        Integer userId = getCurrentUserId();

        log.info("매칭 주제 추천 요청함 userId: {}", userId);
        return matchService.getMatchTopic(userId);
    }

    /**
     * 매칭 연장 요청
     * 
     * POST /api/smalltalk/user/match/extend?accept={accept}
     * 
     * @param accept 연장 수락 여부 (true = 수락, false = 거절)
     * 
     * @return 매칭 연장 신청 됨
     */
//    @PostMapping("/match/extend")
//    public ResponseEntity<?> extendMatch(
//            @RequestParam(value = "accept", required = true, defaultValue = "false") Boolean accept) {
//        Integer userId = getCurrentUserId();
//        return matchService.extendMatch(userId, accept);
//    }


    /**
     * 매칭 연장 요청
     *
     * POST /api/smalltalk/user/match/extend
     *
     * @return 연장됨
     */
    @PostMapping("/match/extend")
    public ResponseEntity<?> extendMatch() {
        Integer userId = getCurrentUserId();

        return matchService.extendMatch(userId);
    }

    /**
     * 매칭 종료 처리
     *
     * POST /api/smalltalk/user/match/end
     *
     * @return 해당 매칭 세션 종료 처리
     */
    @PostMapping("/match/end")
    public ResponseEntity<?> endMatch() {
        Integer userId = getCurrentUserId();

        return matchService.endMatch(userId);
    }
>>>>>>> develop
}
