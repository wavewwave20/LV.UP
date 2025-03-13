package com.alltogether.lvupbackend.smalltalk.ai.controller;

import com.alltogether.lvupbackend.error.AppException;
import com.alltogether.lvupbackend.smalltalk.ai.dao.PersonalityListResponseDto;
import com.alltogether.lvupbackend.smalltalk.ai.dao.ScenarioListResponseDto;
<<<<<<< HEAD
import com.alltogether.lvupbackend.smalltalk.ai.dto.ConversationTextRequestDto;
import com.alltogether.lvupbackend.smalltalk.ai.dto.ConversationTextResponseDto;
import com.alltogether.lvupbackend.smalltalk.ai.dto.RefinementExplanationResponseDto;
=======
import com.alltogether.lvupbackend.smalltalk.ai.dto.*;
import com.alltogether.lvupbackend.smalltalk.ai.service.AiSmalltalkScoreService;
>>>>>>> develop
import com.alltogether.lvupbackend.smalltalk.ai.service.SmalltalkAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/api/smalltalk/ai")
@Slf4j
public class SmallTalkAiController {

    private final SmalltalkAiService smallTalkAiService;

    private final AiSmalltalkScoreService aiSmalltalkScoreService;

    public SmallTalkAiController(SmalltalkAiService smallTalkAiService, AiSmalltalkScoreService aiSmalltalkScoreService) {
        this.smallTalkAiService = smallTalkAiService;
        this.aiSmalltalkScoreService = aiSmalltalkScoreService;
    }


    @GetMapping("/conversation/endall")
    public ResponseEntity<?> endAllConversations() {
        log.info("모든 대화 종료 요청");
        Integer userId = getCurrentUserId();
        smallTalkAiService.endAllConversations(userId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/conversation/end/{aiConversationId}")
    public ResponseEntity<?> endConversation(@PathVariable String aiConversationId) {
        log.info("대화 종료 요청 - ai_conversation_id: {}", aiConversationId);
        try {
            if(aiConversationId == null) {
                throw new AppException("ai_conversation_id가 없습니다.", HttpStatus.BAD_REQUEST);
            }
            int id = Integer.parseInt(aiConversationId);
            smallTalkAiService.endConversation(id);
        } catch (Exception e) {
            throw new AppException("ai_conversation_id가 올바르지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }
<<<<<<< HEAD

    //callback
    @PostMapping("/refinement_explanation/{id}")
    public ResponseEntity<?> getRefinementExplanation(@PathVariable int id, @RequestBody RefinementExplanationResponseDto request) {

        log.info("Refinement Explanation Request - id: {}, request: {}", id, request);

        smallTalkAiService.refinementExplanationWhenCallback(id, request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/conversation/text")
    public ResponseEntity<?> getConversation(@RequestBody ConversationTextRequestDto request) {
        int userId = getCurrentUserId();
        ConversationTextResponseDto result = smallTalkAiService.conversationText(userId, request);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/conversation/end/recent")
    public ResponseEntity<?> endConversation() {
        int userId = getCurrentUserId();
        smallTalkAiService.endConversationRecent(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/conversation/end/all")
    public ResponseEntity<?> endAllConversation() {
        int userId = getCurrentUserId();
        smallTalkAiService.endConversationAll(userId);
        return ResponseEntity.ok().build();
    }
=======
>>>>>>> develop

    @GetMapping("/scenario")
    public ResponseEntity<ScenarioListResponseDto> getScenarios() {
        return ResponseEntity.ok().body(smallTalkAiService.getScenarios());
    }

    @GetMapping("/personality")
    public ResponseEntity<PersonalityListResponseDto> getPersonalities() {
        return ResponseEntity.ok().body(smallTalkAiService.getPersonalities());
    }

<<<<<<< HEAD
=======

    //대화 시작 세팅
    @PostMapping("/conversation/start")
    public ResponseEntity<?> startConversation(@RequestBody StartConversationRequestDto request) {
        int userId = getCurrentUserId();
        StartConversationResponseDto startConversationResponseDto = smallTalkAiService.startConversation(userId, request);
        return ResponseEntity.ok().body(startConversationResponseDto);
    }

    //대화 이어가기
    @PostMapping("/conversation/text")
    public ResponseEntity<?> conversationText(@RequestBody ConversationContinueRequestDto request) {
        ConversationTextResponseDto conversationTextResponseDto = smallTalkAiService.continueConversation(request);
        return ResponseEntity.ok().body(conversationTextResponseDto);
    }

    //대화 답변 힌트 얻기
    @PostMapping("/conversation/hint")
    public ResponseEntity<?> conversationHint(@RequestBody ConversationHintRequestDto request) {
        String hint = smallTalkAiService.getHint(request);

        if(hint == null) {
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.ok().body(new ConversationHintResponseDto(hint));
        }
    }

    //대화 기록 목록 보기
    @GetMapping("/history")
    public ResponseEntity<?> getConversationHistory(
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
        return smallTalkAiService.getHistory(date, page, userId);
    }

    // 특정 월에 대화 기록이 있는 날짜들 조회
    @GetMapping("/history/monthly")
    public ResponseEntity<?> getConversationHistoryCount(
            @RequestParam(value = "date", required = true) String dateStr
    ) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.parse(dateStr, dateTimeFormatter);
        Integer userId = getCurrentUserId();

        return smallTalkAiService.getHistoryCount(userId, date);
    }


    //대화 기록 상세보기
    @GetMapping("/review/{id}")
    public ResponseEntity<?> getConversationReview(@PathVariable Integer id) {
        Integer userId = getCurrentUserId();
        return smallTalkAiService.getReview(userId, id);
    }

    //대화 통계 보기
    @GetMapping("/statistics/{year}/{month}")
    public ResponseEntity<?> getStatistics(@PathVariable Integer year, @PathVariable Integer month) {
        Integer userId = getCurrentUserId();

        ScoreResponseDto dto = aiSmalltalkScoreService.getMonthlyScores(year, month, userId);
        return ResponseEntity.ok().body(dto);
    }




>>>>>>> develop
    private Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("현재 인증된 사용자가 없습니다.");
        }
        return Integer.parseInt(authentication.getName());
    }

}
