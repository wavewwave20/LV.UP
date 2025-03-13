package com.alltogether.lvupbackend.smalltalk.user.controller;

import com.alltogether.lvupbackend.smalltalk.checklist.dto.UserChecklistRequestDTO;
import com.alltogether.lvupbackend.smalltalk.user.service.FeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/smalltalk/user/feedback")
@Slf4j
public class SmallTalkFeedbackController {

    public final FeedbackService feedbackService;

    public SmallTalkFeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    // JWT로부터 사용자 아이디 조회
    private Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("현재 인증된 사용자가 없습니다.");
        }
        return Integer.parseInt(authentication.getName());
    }

    /**
     * 피드백 옵션 조회
     *
     * GET /api/smalltalk/user/feedback/options
     *
     * @return 피드백 옵션 리스트
     */
    @GetMapping("/options")
    public ResponseEntity<?> getFeedbackOptions() {
        System.out.println("옵션 요청");
        return ResponseEntity.ok(feedbackService.getFeedbackOptions());
    }

    /**
     * 피드백 저장
     *
     * POST /api/smalltalk/user/feedback
     *
     * @return 저장 성공 시 201 Created, 실패 시 400 Bad Request
     */
    @PostMapping("")
    public ResponseEntity<?> saveChecklist(@RequestBody UserChecklistRequestDTO request) {
        Integer userId = getCurrentUserId();

        return feedbackService.saveChecklist(userId, request);
    }

    /**
     * 특정 유저의 특정 매치 피드백 상세 조회
     *
     * GET /api/smalltalk/user/feedback/{matchingId}
     *
     * @param matchingId 매칭 아이디
     * @return 피드백 리스트
     */
    @GetMapping("/{matchingId}")
    public ResponseEntity<?> getUserCheklistDetail(@PathVariable("matchingId") Integer matchingId) {
        Integer userId = getCurrentUserId();
        return feedbackService.getUserChecklistDetail(matchingId, userId);
    }

    /**
     * 특정 유저의 모든 피드백 리스트 조회
     *
     * GET /api/smalltalk/user/feedback?page={page}&orderby={orderby}
     *
     * @param page 페이지
     * @param orderby 정렬 기준
     * @return 피드백 리스트
     */
    @GetMapping("")
    public ResponseEntity<?> getUserChecklists(
            @RequestParam(value="page", required = true, defaultValue = "1") Integer page,
            @RequestParam(value = "orderby", required = true, defaultValue = "recently") String orderby) {

        Integer userId = getCurrentUserId();

        log.info("모든 피드백 리스트 조회. userId: {}", userId);
        return feedbackService.getUserChecklists(userId, page, orderby);
    }

}
